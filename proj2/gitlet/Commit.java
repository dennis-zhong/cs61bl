package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Commit implements Serializable {
    Date timestamp;
    String message;
    HashMap<String, String> blobs = new HashMap<>();
    Commit prev;
    File commitFile;
    Branch branch = new Branch();

    public Commit() {
        timestamp = new Date(0);
        message = "initial commit";
        commitFile = new File(".gitlet/commit/"+this.toString());
    }

    public Commit(HashMap<String, String> blobs, String message, Commit prev) {
        timestamp = new Date();
        this.message = message;
        this.blobs = blobs;
        this.prev = prev;
        commitFile = new File(".gitlet/commit/"+this.toString());
    }

    public void saveCommit() {
        Utils.writeObject(commitFile, this);
    }

    public Date getDate() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Branch getBranch() {
        return this.branch;
    }
    public void setBranch(Branch branch) {
        this.branch = branch;
        this.saveCommit();
    }

    public HashMap<String, String> getBlobs() {
        return blobs;
    }

    public static Commit readCommit(String str) {
        return Utils.readObject(new File(".gitlet/commit/"+str), Commit.class);
    }

    public String getID() {
        return commitFile.getName();
    }

    @Override
    public String toString() {
        return Utils.sha1(Utils.serialize(this));
    }
}
