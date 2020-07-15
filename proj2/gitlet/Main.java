package gitlet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author
 */
public class Main {

    /** Current Working Directory. */
    static final File CWD = new File(".");

    /** Main metadata folder. */
    static final File GITLET_FOLDER = new File(".gitlet");
    static final File STAGING_FOLDER = new File(".gitlet/stage");
    static final File COMMIT_FOLDER = new File(".gitlet/commit");
    static final File BLOB_FOLDER = new File(".gitlet/blobs");
    static final File HEAD = new File("HEAD");

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String[] args) {
        if (args.length == 0) {
            exitWithError("Please enter a command.");
        }

        switch (args[0]) {
            case "init":
                initialize(args);
                break;
            case "add":
                addFile(args);
                break;
            case "commit":
                commit(args);
                break;
            case "log":
                log(args);
                break;
            default:
                exitWithError("No command with that name exists.");
        }
        return;
    }

    //initialize gitlet
    public static void initialize(String[] args) {
        validateNumArgs(args, 1);
        if(GITLET_FOLDER.exists()) {
            exitWithError("A Gitlet version-control system already exists in the current directory.");
        }
        GITLET_FOLDER.mkdir();
        STAGING_FOLDER.mkdir();
        COMMIT_FOLDER.mkdir();
        BLOB_FOLDER.mkdir();
        try {
            HEAD.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Commit initial = new Commit();
        initial.saveCommit();
        setHead(initial);
    }

    //check if in gitlet dir
    public static void checkInit() {
        if (!GITLET_FOLDER.exists()) {
            exitWithError("Not in an initialized Gitlet directory.");
        }
    }

    //add blob to staging area
    public static void addFile(String[] args) {
        validateNumArgs(args, 2);
        checkInit();
        if (!(new File(args[1]).exists())) {
            exitWithError("File does not exist");
        }
        Commit head = Utils.readObject(HEAD, Commit.class);
        Blob newBlob = new Blob(args[1]);
        if (head.getBlobs().get(args[1]) != null) {//check if prev blob already in commit head
            if (newBlob.getContents().equals(head.getBlobs().get(args[1]).getContents())) {
                return;//if contents r same dont add it
            }
        }
        Utils.writeObject(new File(".gitlet/stage/"+newBlob.getName()), newBlob);
    }

    public static void commit(String[] args) {
        checkInit();
        if(STAGING_FOLDER.list().length==0) {
            exitWithError("No changes added to the commit");
        }
        if (args.length == 1) {
            exitWithError("Please enter a commit message");
        }
        validateNumArgs(args, 2);

        List<Blob> lst = new ArrayList<>();
        for (File file: STAGING_FOLDER.listFiles()) {
            lst.add(Utils.readObject(file, Blob.class));
            file.delete();
        }
        Commit com = new Commit(lst, args[1], getHead());
        com.saveCommit();
        setHead(com);
    }

    public static Commit getHead() {
        return Utils.readObject(HEAD, Commit.class);
    }

    public static void setHead(Commit com) {
        Utils.writeObject(HEAD, com);
    }

    public static void log(String[] args) {
        validateNumArgs(args, 1);
        checkInit();
        File pointer = HEAD;
        Commit currCom;
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM d HH:mm:ss y Z");
        for(int i = 0; i<COMMIT_FOLDER.listFiles().length; i++) {
            currCom = Utils.readObject(pointer, Commit.class);
            System.out.println("===\ncommit "+currCom.toString()+
                    "\nDate: "+formatter.format(currCom.getDate())+"\n"
                    +currCom.getMessage());
            if (currCom.prev != null) {
                pointer = currCom.prev.commitFile;
            }
        }
    }

    /**
     * Prints out MESSAGE and exits with error code 0.
     * @param message message to print
     */
    public static void exitWithError(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(0);
    }

    /**
     *
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            exitWithError("Incorrect operands.");
        }
    }

}
