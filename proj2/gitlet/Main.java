package gitlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    static final File REMOVE_FOLDER = new File(".gitlet/removeStage");
    static final File BRANCH_FOLDER = new File(".gitlet/branches");
    static final File HEAD = new File(".gitlet/HEAD");

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
            case "global-log":
                globalLog(args);
                break;
            case "rm":
                remove(args);
                break;
            case "find":
                find(args);
                break;
            case "status":
                status(args);
                break;
            case "checkout":
                checkout(args);
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
        //STAGING_FOLDER.mkdir();
        COMMIT_FOLDER.mkdir();
        BLOB_FOLDER.mkdir();
        BRANCH_FOLDER.mkdir();
        //REMOVE_FOLDER.mkdir();
        try {
            HEAD.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Commit initial = new Commit();
        Branch master = new Branch();
        initial.saveCommit();
        setHead(initial);
        master.setHead(initial);
        master.saveBranch();
        Stage stagingFolder = new Stage("stage");
        Stage removeFolder = new Stage("removeStage");
        stagingFolder.saveStage();
        removeFolder.saveStage();
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
        if (!(new File("./"+args[1]).exists())) {
            exitWithError("File does not exist");
        }

        Blob newBlob = new Blob(args[1]);
        Stage staging = Utils.readObject(new File(".gitlet/stage"), Stage.class);
        if(checkDiffBlob(newBlob)) {
            newBlob.saveBlob();
            staging.putOnStage(newBlob);
            staging.saveStage();
        } else {
            staging.unStage(newBlob);
        }
    }

    public static boolean checkDiffBlob(Blob blob) {
        Commit head = getHead();
        if (head.getBlobs().get(blob.getName()) != null) {//check if prev blob already in commit head
            if (blob.getContents().equals(
                    Blob.getBlobObj(head.getBlobs().get(blob.getName())).getContents())) {
                return false;//if contents r same dont add it
            }
        }
        return true;
    }

    public static void commit(String[] args) {
        checkInit();
        if(getStage().getBlobs().isEmpty()) {
            exitWithError("No changes added to the commit");
        }
        if (args.length == 1) {
            exitWithError("Please enter a commit message");
        }
        validateNumArgs(args, 2);

        HashMap<String, String> newBlobs = getHead().getBlobs();
        for (Blob blob: getStage().getBlobs().values().stream()
                .map(x->Utils.readObject(new File(".gitlet/blobs/"+x), Blob.class)).collect(Collectors.toList())) {
            newBlobs.put(blob.getName(), blob.getBlobFile().getName());
        }
        Commit com = new Commit(newBlobs, args[1], getHead());
        Branch currBranch = getHead().getBranch();
        com.saveCommit();
        setHead(com);
        currBranch.setHead(com);
        getStage().emptyStage();
    }

    public static Commit getHead() {
        return Utils.readObject(HEAD, Commit.class);
    }

    public static void setHead(Commit com) {
        HEAD.delete();
        Utils.writeObject(HEAD, com);
    }

    public static Stage getStage() {
        return Utils.readObject(STAGING_FOLDER, Stage.class);
    }

    public static Stage getRemoved() {
        return Utils.readObject(REMOVE_FOLDER, Stage.class);
    }

    public static void log(String[] args) {
        validateNumArgs(args, 1);
        checkInit();
        File pointer = HEAD;
        Commit currCom;
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM d HH:mm:ss y Z");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT-800"));
        for(int i = 0; i<COMMIT_FOLDER.listFiles().length; i++) {
            currCom = Utils.readObject(pointer, Commit.class);
            System.out.println("===\ncommit "+currCom.getID()+
                    "\nDate: "+formatter.format(currCom.getDate())+"\n"
                    +currCom.getMessage()+"\n");
            if (currCom.prev != null) {
                pointer = currCom.prev.commitFile;
            }
        }
    }

    public static void globalLog(String[] args) {
        validateNumArgs(args, 1);
        checkInit();
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM d HH:mm:ss y Z");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT-800"));
        Commit currCom;
        for(File file: COMMIT_FOLDER.listFiles()) {
            currCom = Utils.readObject(file, Commit.class);
            System.out.println("===\ncommit "+currCom.toString()+
                    "\nDate: "+formatter.format(currCom.getDate())+"\n"
                    +currCom.getMessage()+"\n");
        }
    }

    public static void remove(String[] args) {
        validateNumArgs(args, 2);
        checkInit();
        Stage stageFile = getStage();
        Blob newBlob = Blob.getBlobObj(getHead().getBlobs().get(args[1]));//finds blob in head com
        if(newBlob == null && !stageFile.getBlobs().keySet().contains(args[1])) {
            exitWithError("No reason to remove the file");
        }

        newBlob = Blob.getBlobObj(stageFile.getBlobs().get(args[1]));
        if (stageFile.getBlobs().keySet().contains(args[1])) {
            stageFile.unStage(newBlob);
        }
        if(newBlob != null) {
            getRemoved().putOnStage(newBlob);//puts in remove stage
            File currFile = new File(args[1]);
            if(currFile.exists()) {
                currFile.delete();
            }
        }
    }

    public static void find(String[] args) {
        validateNumArgs(args, 2);
        checkInit();
        for(File file: COMMIT_FOLDER.listFiles()) {
            Commit com = Utils.readObject(file, Commit.class);
            if(com.getMessage().equals(args[1])) {
                System.out.println(com.getID());
            }
        }
    }

    public static void status(String[] args) {
        validateNumArgs(args, 1);
        checkInit();
        System.out.println("=== Branches ===");
        for(File branch: BRANCH_FOLDER.listFiles()) {
            Branch br = Utils.readObject(branch, Branch.class);
            if(br.getHead().getID().equals(getHead().getID())) {
                System.out.print("*");
            }
            System.out.println(br);
        }
        System.out.println("\n=== Staged Files ===");
        HashSet<String> mod1 = new HashSet<>();//cond 1
        HashSet<String> mod2 = new HashSet<>();//cond 2
        HashSet<String> stagedFiles = new HashSet<>();
        HashSet<String> deleted1 = new HashSet<>();//cond 3
        HashSet<String> deleted2 = new HashSet<>();//cond 4
        ArrayList<String> untracked = new ArrayList<>();
        for(String str: getHead().getBlobs().keySet()) {
            deleted2.add(str);
        }
        for(String name: getStage().getBlobs().keySet()) {
            stagedFiles.add(name);
        }
        for(File file: CWD.listFiles()) {
            if(file.isDirectory()) {
                continue;
            }
            Blob blob = new Blob(file.getName());
            deleted2.remove(file.getName());
            if(getHead().getBlobs().containsKey(blob.getName()) && checkDiffBlob(blob)
                    && !stagedFiles.contains(file.getName())) {
                mod1.add(file.getName());
            }
            untracked.add(file.getName());
        }
        for(String str: getHead().getBlobs().keySet()) {
            untracked.remove(str);
        }
        for(String file: getStage().getBlobs().values()) {
            Blob curr = Blob.getBlobObj(file);
            System.out.println(curr.getName());
            untracked.remove(curr.getName());
            File fileCWD = new File("./"+curr.getName());
            if (!fileCWD.exists()) {
                deleted1.add(fileCWD.getName());
            } else if(!curr.getContents().equals(Utils.readContentsAsString(fileCWD))) {
                mod2.add(curr.getName());
            } else if (curr.getContents().equals(Utils
                    .readContentsAsString(fileCWD))) {
                mod2.remove(curr.getName());
            }
        }
        System.out.println("\n=== Removed Files ===");
        for(String file: getRemoved().getBlobs().keySet()) {
            System.out.println(file);
            deleted2.remove(file);
            mod1.remove(file);
        }
        System.out.println("\n=== Modifications Not Staged For Commit===");
        deleted1.addAll(deleted2);
        List<String> deletedLst = deleted1.stream().map(x->x+" (deleted)").sorted().collect(Collectors.toList());
        mod1.addAll(mod2);
        List<String> modLst = mod1.stream().map(x->x+" (modified)").sorted().collect(Collectors.toList());
        deletedLst.addAll(modLst);//complete lst now
        Collections.sort(deletedLst);
        for(String str: deletedLst) {
            System.out.println(str);
        }
        System.out.println("\n=== Untracked Files ===");
        for(String file: untracked) {
            System.out.println(file);
        }
    }

    public static void checkout(String[] args) {
        Blob overWrite = null;
        File dest = null;
        if (args.length == 3) {
            overWrite = Blob.getBlobObj(getHead().getBlobs().get(args[2]));
            dest = new File("./" + args[2]);
        } else if (args.length == 4) {
            Commit currCom = getHead().getBranch().getComFromFile(args[1]);
            if (currCom == null) {
                for(File file: COMMIT_FOLDER.listFiles()) {
                    if (file.getName().startsWith(args[1])) {
                        currCom = Commit.readCommit(file.getName());
                    }
                }
                if (currCom == null) {
                    exitWithError("No commit with that id exists.");
                }
            }
            overWrite = Blob.getBlobObj(currCom.getBlobs().get(args[3]));
            dest = new File("./" + args[3]);
        } else {
            validateNumArgs(args, 2);
            Branch br = new Branch(args[1]);
            if(!br.getFile().exists()) {
                exitWithError("No such branch exists");
            } if(getHead().getBranch().getFile().equals(br.getFile())) {
                exitWithError("No need to checkout the current branch");
            }
            ArrayList<String> untracked = new ArrayList<>();
            ArrayList<String> tracked = new ArrayList<>();
            for(File file: CWD.listFiles()) {
                untracked.add(file.getName());
            } for(String str: getHead().getBlobs().keySet()) {
                untracked.remove(str);
                tracked.add(str);
            } for(String file: getStage().getBlobs().values()) {
                untracked.remove(Blob.getBlobObj(file).getName());
            }
            if(!untracked.isEmpty()) {
                exitWithError("There is an untracked file in the way; delete it, or add and commit it first.");
            }
            for(String blob: br.getHead().getBlobs().keySet()) {
                Blob currBlob = Blob.getBlobObj(blob);
                File currFile = new File("./"+currBlob.getName());
                if (currFile.exists()) {
                    Utils.writeContents(currFile, currBlob);
                    tracked.remove(currBlob.getName());
                }
            }
            setHead(br.head);
            for(String str: tracked) {//delete files in curr dir that r tracked in previous commit
                new File("./"+str).delete();
            }
            getStage().emptyStage();
            return;
        }
        if (overWrite == null) {
            exitWithError("File does not exist in that commit.");
        }
        Utils.writeContents(dest, overWrite.getContents());
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
