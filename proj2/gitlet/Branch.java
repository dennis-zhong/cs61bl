package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.stream.Collectors;

public class Branch implements Serializable {
    HashMap<String, Commit> comBranch;
    String name;
    File branchFile;
    Commit head;

    public Branch() {
        name = "master";
        comBranch = new HashMap<>();
        branchFile = new File(".gitlet/branches/"+name);
    }

    public Branch(String name) {
        this.name = name;
        comBranch = new HashMap<>();
        branchFile = new File(".gitlet/branches/"+name);
    }

    public String getName(){
        return name;
    }

    public void setHead(Commit com) {
        comBranch.put(com.getID(), com);
        head = com;
        this.saveBranch();
    }

    public Commit getHead(){
        return head;
    }

    public File getFile(){
        return branchFile;
    }

    public Commit getCom(String id){
        return comBranch.get(id);
    }

    public static Branch getBranch(String name) {
        File file = new File(".gitlet/branches/"+name);
        if(!file.exists()) {
            return null;
        }
        return Utils.readObject(file, Branch.class);
    }

    public void saveBranch() {
        Utils.writeObject(branchFile, this);
    }

    public boolean contains(Commit com) {
        return comBranch.keySet().contains(com.getID());
    }

    @Override
    public String toString() {
        return name;
    }
}
