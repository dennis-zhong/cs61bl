package gitlet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
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
            case "branch":
                branch(args);
                break;
            case "rm-branch":
                removeBranch(args);
                break;
            case "reset":
                reset(args);
                break;
            case "merge":
                merge(args);
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
        if (!(new File("./"+args[1]).exists())) {//has to be in cwd to add
            exitWithError("File does not exist.");
        }
        if(getRemoved().getBlobs().get(args[1]) != null) {
            Blob temp = new Blob();
            temp.name = args[1];
            getRemoved().unStage(temp);
            return;
        }
        Blob newBlob = new Blob(args[1]);
        Stage staging = Utils.readObject(new File(".gitlet/stage"), Stage.class);
        if(checkDiffBlob(newBlob)) {
            newBlob.saveBlob();
            staging.putOnStage(newBlob);
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
        if(getStage().getBlobs().isEmpty() && getRemoved().getBlobs().isEmpty()) {
            exitWithError("No changes added to the commit.");
        }
        validateNumArgs(args, 2);
        if(args[1].equals("")) {
            exitWithError("Please enter a commit message.");
        }

        HashMap<String, String> newBlobs = getHead().getBlobs();
        for(Blob blob: getStage().getBlobs().values().stream()
                .map(x->Blob.getBlobObj(x)).collect(Collectors.toList())) {
            newBlobs.put(blob.getName(), blob.getBlobFile().getName());
        }
        for(String str: getRemoved().getBlobs().values()) {
            newBlobs.remove(Blob.getBlobObj(str).getName());
        }
        Commit com = new Commit(newBlobs, args[1], getHead());
        Branch currBranch = getHeadBranch();
        com.saveCommit();
        currBranch.setHead(com);
        setHead(currBranch);
        getStage().emptyStage();
        getRemoved().emptyStage();
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
            System.out.print("===\ncommit "+currCom.getID());
            if(currCom.prev2 != null) {
                System.out.print("\nMerge: "+currCom.prev.getID().substring(0, 7)
                        +" "+currCom.prev2.getID().substring(0, 7));
            }
            System.out.print("\nDate: "+formatter.format(currCom.getDate()));
            System.out.print("\n"+currCom.getMessage()+"\n\n");
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
            System.out.print("===\ncommit "+currCom.getID());
            if(currCom.prev2 != null) {
                System.out.print("\nMerge: "+currCom.prev.getID().substring(0, 7)
                        +" "+currCom.prev2.getID().substring(0, 7));
            }
            System.out.print("\nDate: "+formatter.format(currCom.getDate()));
            System.out.print("\n"+currCom.getMessage()+"\n\n");
        }
    }

    public static void remove(String[] args) {
        validateNumArgs(args, 2);
        checkInit();
        Stage stageFile = getStage();
        Blob newBlob = Blob.getBlobObj(getHead().getBlobs().get(args[1]));//finds blob in head com
        if(newBlob.isEmpty() && !stageFile.getBlobs().keySet().contains(args[1])) {
            exitWithError("No reason to remove the file.");
        }

        if (stageFile.getBlobs().keySet().contains(args[1])) {
            stageFile.unStage(Blob.getBlobObj(stageFile.getBlobs().get(args[1])));
        }
        if(!newBlob.isEmpty()) {
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
        int i = 0;
        for(File file: COMMIT_FOLDER.listFiles()) {
            Commit com = Utils.readObject(file, Commit.class);
            if(com.getMessage().equals(args[1])) {
                System.out.println(com.getID());
                i++;
            }
        }
        if(i==0) {
            exitWithError("Found no commit with that message.");
        }
    }

    public static void status(String[] args) {
        validateNumArgs(args, 1);
        checkInit();
        System.out.println("=== Branches ===");
        for(String branch: Utils.plainFilenamesIn(BRANCH_FOLDER)) {
            Branch br = Utils.readObject(new File(".gitlet/branches/"+branch), Branch.class);
            if(br.toString().equals(getHeadBranch().toString())) {
                System.out.print("*");
            }
            System.out.println(br);
        }
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
        System.out.println("\n=== Staged Files ===");
        for(String file: getStage().getBlobs().values().stream()
                .sorted().collect(Collectors.toList())) {
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
            if(new File("./"+file).exists() && !untracked.contains(file)) {//in case you go to another commit where the removed file isnt, prevents doubling
                untracked.add(file);
            }
        }
        System.out.println("\n=== Modifications Not Staged For Commit ===");
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
        checkInit();
        Blob overWrite;
        File dest;
        if (args.length == 3) {//we check --
            validateNumArgs(args, 3);
            if(!args[1].equals("--")) {
                exitWithError("Incorrect operands.");
            }
            overWrite = Blob.getBlobObj(getHead().getBlobs().get(args[2]));
            dest = new File("./" + args[2]);
        } else if (args.length == 4) {//what should be status of checked out files
            validateNumArgs(args, 4);
            if(!args[2].equals("--")) {
                exitWithError("Incorrect operands.");
            }
            Commit currCom = getComFromFile(args[1]);
            overWrite = Blob.getBlobObj(currCom.getBlobs().get(args[3]));
            dest = new File("./" + args[3]);
        } else {
            validateNumArgs(args, 2);
            Branch br = Branch.getBranch(args[1]);
            if(br == null) {
                exitWithError("No such branch exists.");
            } if(getHeadBranch().getFile().equals(br.getFile())) {
                exitWithError("No need to checkout the current branch.");
            }
            checkUntracked(br.getHead());
            ArrayList<String> tracked = new ArrayList<>();
            for(String str: getHead().getBlobs().keySet()) {
                tracked.add(str);
            }
            for(String blob: br.getHead().getBlobs().values()) {
                Blob currBlob = Blob.getBlobObj(blob);
                File currFile = new File("./"+currBlob.getName());
                tracked.remove(currBlob.getName());
                Utils.writeContents(currFile, currBlob.getContents());
            }
            setHead(br);
            for(String str: tracked) {//delete files in curr dir that r tracked in previous commit
                new File("./" + str).delete();
                //if(!br.getHead().getBlobs().keySet().contains(str)) {
                //}
            }
            getStage().emptyStage();
            return;
        }
        if (overWrite.isEmpty()) {
            exitWithError("File does not exist in that commit.");
        }
        Utils.writeContents(dest, overWrite.getContents());
    }

    public static void checkUntracked(Commit com) {
        ArrayList<String> untracked = new ArrayList<>();
        for(String file: com.getBlobs().keySet()) {
            untracked.add(file);
        } for(String str: getHead().getBlobs().keySet()) {
            untracked.remove(str);
        } for(String file: getStage().getBlobs().values()) {
            untracked.remove(Blob.getBlobObj(file).getName());
        } for(int i = 0; i < untracked.size(); i++ ) {
            if(!new File(untracked.get(i)).exists()) {
                untracked.remove(untracked.get(i));
                i--;
            }
        }
        //System.out.println(untracked);
        if(!untracked.isEmpty()) {
                /*for(String str: untracked) {
                    System.out.println(str);
                }*/
            exitWithError("There is an untracked file in the way; delete it, or add and commit it first.");
        }
    }

    public static void branch(String[] args) {
        validateNumArgs(args, 2);
        checkInit();
        if(new File(".gitlet/branches/"+args[1]).exists()) {
            exitWithError("A branch with that name already exists.");
        }
        Branch branch = new Branch(args[1]);
        branch.setHead(getHead());
        branch.saveBranch();
    }

    public static void removeBranch(String[] args) {
        validateNumArgs(args, 2);
        checkInit();
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
        checkInit();
        Commit curr = getComFromFile(args[1]);
        checkUntracked(curr);
        ArrayList<String> tracked = new ArrayList<>();
        for(String str: getHead().getBlobs().keySet()) {
            tracked.add(str);
        }
        for(String blob: curr.getBlobs().values()) {
            Blob currBlob = Blob.getBlobObj(blob);
            File currFile = new File("./"+currBlob.getName());
            tracked.remove(currBlob.getName());
            Utils.writeContents(currFile, currBlob.getContents());
        }
        for(String str: tracked) {//delete files in curr dir that r tracked in previous commit
            new File("./" + str).delete();
            //if(!br.getHead().getBlobs().keySet().contains(str)) {
            //}
        }
        Branch br = getHeadBranch();
        br.setHead(curr);
        setHead(br);
        getStage().emptyStage();
    }

    public static void merge(String[] args) {
        validateNumArgs(args, 2);
        checkInit();
        if(!getStage().isEmpty() || !getRemoved().isEmpty()) {
            exitWithError("You have uncommitted changes.");
        }
        Branch br = Branch.getBranch(args[1]);
        if(br == null) {
            exitWithError("A branch with that name does not exist.");
        }
        if(br.getName().equals(getHeadBranch().getName())) {
            exitWithError("Cannot merge a branch with itself.");
        }
        Commit LCA = Commit.findLCA(getHead(), br.getHead());//getLCA(br);
        checkMergeUntracked(LCA, br.getHead());
        /*Commit testLCA = Commit.findLCA(getHead(), br.getHead());
        System.out.println(testLCA.getID());
        if(testLCA.equals(LCA)) {
            System.out.println("itwork");
        }*/
        if(LCA.equals(getHead())) {
            checkout(new String[]{"checkout", br.getName()});
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        if(LCA.equals(br.getHead())) {
            exitWithError("Given branch is an ancestor of the current branch.");
        }
        /*
        HashMap<String, String> modifiedH = new HashMap<>();
        HashMap<String, String> modifiedB = new HashMap<>();
        HashMap<String, String> notModifiedH = new HashMap<>();
        HashMap<String, String> notModifiedB = new HashMap<>();

        for(String id: getHead().getBlobs().values()) {
            Blob curr = Blob.getBlobObj(id);
            if(Blob.getBlobObj(LCA.getBlobs().get(curr.getName()))
                    .equals(curr.getContents())) {
                modifiedH.put(curr.getName(), id);
            } else {
                notModifiedH.put(curr.getName(), id);
            }
        }*/
        ArrayList<String> everyFile = new ArrayList<>();
        for(String blob: getHead().getBlobs().keySet()) {
            everyFile.add(blob);
        }
        for(String blob: br.getHead().getBlobs().keySet()) {
            if(!everyFile.contains(blob)) {
                everyFile.add(blob);
            }
        }
        for(String blob: LCA.getBlobs().keySet()) {
            if(!everyFile.contains(blob)) {
                everyFile.add(blob);
            }
        }
        Blob headBlob;
        Blob brBlob;
        Blob comBlob;
        boolean conflict = false;
        for(String file: everyFile) {
            headBlob = Blob.getBlobObj(getHead().getBlobs().get(file));
            brBlob = Blob.getBlobObj(br.getHead().getBlobs().get(file));
            comBlob = Blob.getBlobObj(LCA.getBlobs().get(file));
            if(headBlob.equals(brBlob) && comBlob.equals(brBlob) && comBlob.equals(headBlob)) {
                continue;
            } else if(comBlob.equals(headBlob) && brBlob.isEmpty()
                    && !comBlob.equals(brBlob) && !headBlob.equals(brBlob)) {
                //Any files present at the split point, unmodified in the current branch, and absent in the given branch should be removed (and untracked).
                remove(new String[]{"remove", file});
            } else if(!headBlob.equals(brBlob) && !brBlob.equals(comBlob)
                    && headBlob.equals(comBlob)) {
                //Any files that were not present at the split point and are present only in the given branch should be checked out and staged.
                //Any files that have been modified in the given branch since the split point, but not modified in the current branch since the split point
                checkout(new String[]{"checkout", br.getHead().getID(), "--", file});
                getStage().putOnStage(brBlob);
            } else if(comBlob.equals(brBlob) && !headBlob.equals(brBlob)
                    && !comBlob.equals(headBlob)) {
                //Any files present at the split point, unmodified in the given branch, and absent in the current branch should remain absent.
                //Any files that have been modified in the current branch but not in the given branch since the split point should stay as they are.
                continue;
            } else if(brBlob.equals(headBlob) && !brBlob.equals(comBlob)
                    && !headBlob.equals(comBlob)) {
                //should you untrack a removed file/save the fact it was removed in a commit
                //Any files that have been modified in both the current and given branch in the same way
                continue;
            } else if(comBlob.isEmpty() && brBlob.isEmpty() && !headBlob.isEmpty()) {
                continue;
            } else if(!comBlob.equals(brBlob) && !brBlob.equals(headBlob)
                    && !comBlob.equals(headBlob)) {
                Blob newBlob = new Blob();
                newBlob.name = headBlob.name;
                newBlob.contents = "<<<<<<< HEAD\n"
                        +headBlob.getContents()+"=======\n"
                        +brBlob.getContents()+">>>>>>>\n";
                newBlob.blobFile = new File(".gitlet/blobs/"+newBlob.toString());
                newBlob.setContents(newBlob.getContents());
                conflict = true;
                getStage().putOnStage(newBlob);
            }
        }
        commit(new String[]{"commit", "Merged "+br+" into "+getHeadBranch()+"."});
        if(conflict) {
            System.out.println("Encountered a merge conflict.");
        }
        Branch newbr = getHeadBranch();
        newbr.getHead().setPrev2(br.getHead());
        setHead(newbr);
        //System.out.println(getHead().prev);
        //System.out.println(getHead().prev2);
    }

    public static void checkMergeUntracked(Commit LCA, Commit br) {
        ArrayList<String> untracked = new ArrayList<>();
        for(String file: br.getBlobs().keySet()) {
            untracked.add(file);
        } for(String file: LCA.getBlobs().keySet()) {//in case some stuff happens where head loses track, but lca has file
            if(!untracked.contains(file)) {
                untracked.add(file);
            }
        } for(String str: getHead().getBlobs().keySet()) {
            untracked.remove(str);
        } for(int i = 0; i < untracked.size(); i++ ) {//get rid of unnecessary untracked (if file doesn't exist no need to track)
            if(!new File(untracked.get(i)).exists()) {
                untracked.remove(untracked.get(i));
                i--;
            }
        }
        //System.out.println(untracked);
        if(!untracked.isEmpty()) {
            exitWithError("There is an untracked file in the way; delete it, or add and commit it first.");
        }
    }

    public static Commit getLCA(Branch branch) {//bad LCA
        Commit currH = getHead();
        Commit compare = branch.getHead();
        while(currH.prev != null) {
            while(compare.prev != null) {
                if(currH.getID().equals(compare.getID())) {
                    return currH;
                }
                compare = compare.prev;
            }
            compare = branch.getHead();
            currH = currH.prev;
        }
        return currH;
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
