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
    static final File BRANCH_POINTER = new File(".gitlet/currB");

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
            case "branch":
                branch(args);
                break;
            case "rm-branch":
                removeBranch(args);
                break;
            case "reset":
                reset(args);
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
            //BRANCH_POINTER.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Commit initial = new Commit();
        Branch master = new Branch();
        initial.saveCommit();
        master.setHead(initial);
        setHead(master);

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
        Branch currBranch = getHeadBranch();
        com.saveCommit();
        currBranch.setHead(com);
        setHead(currBranch);
        getStage().emptyStage();
    }

    public static Commit getHead() {
        return Utils.readObject(HEAD, Branch.class).getHead();
    }

    public static Branch getHeadBranch() {
        return Utils.readObject(HEAD, Branch.class);
    }

    public static void setHead(Branch br) {
        Utils.writeObject(HEAD, br);
    }

    public static Stage getStage() {
        return Utils.readObject(STAGING_FOLDER, Stage.class);
    }

    public static Stage getRemoved() {
        return Utils.readObject(REMOVE_FOLDER, Stage.class);
    }
    public static Commit getComFromFile(String name){
        File file = new File(".gitlet/commit/"+name);
        Commit currCom = null;
        if(file.exists()) {
            currCom = Utils.readObject(file, Commit.class);
        }
        if (currCom == null) {
            for(File comfile: COMMIT_FOLDER.listFiles()) {
                if (comfile.getName().startsWith(name)) {
                    currCom = Commit.readCommit(comfile.getName());
                }
            }
            if (currCom == null) {
                exitWithError("No commit with that id exists.");
            }
        }
        return currCom;
    }

    public static void log(String[] args) {
        validateNumArgs(args, 1);
        checkInit();
        Commit currCom = getHead();
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM d HH:mm:ss y Z");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT-800"));
        while (currCom != null) {
            System.out.println("===\ncommit "+currCom.getID()+
                    "\nDate: "+formatter.format(currCom.getDate())+"\n"
                    +currCom.getMessage()+"\n");
            currCom = currCom.prev;
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
            if(br.toString().equals(getHeadBranch().toString())) {
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
            Commit currCom = getComFromFile(args[1]);
            overWrite = Blob.getBlobObj(currCom.getBlobs().get(args[3]));
            dest = new File("./" + args[3]);
        } else {
            validateNumArgs(args, 2);
            Branch br = Branch.getBranch(args[1]);
            if(br == null) {
                exitWithError("No such branch exists");
            } if(getHeadBranch().getFile().equals(br.getFile())) {
                exitWithError("No need to checkout the current branch");
            }
            ArrayList<String> untracked = new ArrayList<>();
            ArrayList<String> tracked = new ArrayList<>();
            for(File file: CWD.listFiles()) {
                if(file.isDirectory() || !file.getName().endsWith(".txt")) {//remove the .txt later
                    continue;
                }
                untracked.add(file.getName());
            } for(String str: getHead().getBlobs().keySet()) {
                untracked.remove(str);
                tracked.add(str);
            } for(String file: getStage().getBlobs().values()) {
                untracked.remove(Blob.getBlobObj(file).getName());
            }
            if(!untracked.isEmpty()) {
                for(String str: untracked) {
                    System.out.println(str);
                }
                exitWithError("There is an untracked file in the way; delete it, or add and commit it first.");
            }
            for(String blob: br.getHead().getBlobs().values()) {
                Blob currBlob = Blob.getBlobObj(blob);
                File currFile = new File("./"+currBlob.getName());
                if (currFile.exists()) {
                    Utils.writeContents(currFile, currBlob.getContents());
                    tracked.remove(currBlob.getName());
                }
            }
            setHead(br);
            for(String str: tracked) {//delete files in curr dir that r tracked in previous commit
                if(!br.getHead().getBlobs().keySet().contains(str)) {
                    new File("./"+str).delete();
                }
            }
            getStage().emptyStage();
            return;
        }
        if (overWrite == null) {
            exitWithError("File does not exist in that commit.");
        }
        Utils.writeContents(dest, overWrite.getContents());
    }

    public static void branch(String[] args) {
        validateNumArgs(args, 2);
        if(new File(".gitlet/branches/"+args[1]).exists()) {
            exitWithError("A branch with that name already exists.");
        }
        Branch branch = new Branch(args[1]);
        branch.setHead(getHead());
        branch.saveBranch();
    }

    public static void removeBranch(String[] args) {
        validateNumArgs(args, 2);
        File branchFile = new File(".gitlet/branches/"+args[1]);
        if(!branchFile.exists()) {
            exitWithError("A branch with that name does not exist.");
        } else if(branchFile.equals(getHeadBranch().getFile())){
            exitWithError("Cannot remove the current branch.");
        } else {
            branchFile.delete();
        }
    }

    public static void reset(String[] args) {
        validateNumArgs(args, 2);
        Commit curr = getComFromFile(args[1]);
        for(String str: curr.getBlobs().values()) {
            Blob.getBlobObj(str);
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
