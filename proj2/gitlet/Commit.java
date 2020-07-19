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
    Commit prev2;
    File commitFile;

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

    public Commit getPrev2() {
        return prev2;
    }

    public void setPrev2(Commit com) {
        this.prev2 = com;
        saveCommit();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commit commit = (Commit) o;
        return Objects.equals(timestamp, commit.timestamp) &&
                Objects.equals(message, commit.message) &&
                Objects.equals(blobs, commit.blobs) &&
                Objects.equals(prev, commit.prev) &&
                Objects.equals(commitFile, commit.commitFile);
    }

    public static Commit findLCA(Commit head, Commit compare, int depth) {
        return new Commit();
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, message, blobs, prev, commitFile);
    }

    @Override
    public String toString() {
        return Utils.sha1(Utils.serialize(this));
    }
}
