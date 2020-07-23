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

    public static Commit findLCA(Commit head, Commit compare) {
        HashMap<String, Commit> ancestors = collectAncestors(head, compare, new HashMap<String, Commit>());
        int shortest = Integer.MAX_VALUE;
        Commit save = null;
        int dist;
        for(Commit curr: ancestors.values()) {
            dist = calcDist(head, curr, 0);
            if(dist<shortest) {
                shortest = dist;
                save = curr;
            }
        }
        return save;
    }

    public static HashMap<String, Commit> collectAncestors(Commit head, Commit comp, HashMap<String, Commit> collective) {
        if(head.equals(new Commit()) || comp.equals(new Commit())) {
            collective.put(new Commit().getID(), new Commit());
            return collective;
        } else if(head.getID().equals(comp.getID())) {
            collective.put(head.getID(), head);
            return collective;
        } else {
            HashMap<String, Commit> comps1 = collectAncestors(head.prev, comp.prev, collective);
            HashMap<String, Commit> comps2 = collectAncestors(head, comp.prev, collective);
            HashMap<String, Commit> comps3 = collectAncestors(head.prev, comp, collective);
            collective.putAll(comps1);
            collective.putAll(comps2);
            collective.putAll(comps3);
            if(head.prev2 != null) {
                HashMap<String, Commit> comps4 = collectAncestors(head.prev2, comp, collective);
                collective.putAll(comps4);
            }
            if(comp.prev2 != null) {
                collective.putAll(collectAncestors(head, comp.prev2, collective));
            }
            return collective;
        }
    }

    public static int calcDist(Commit start, Commit end, int depth) {
        if(start.equals(new Commit()) || start.equals(end)) {
            return depth;
        } else {
            depth+=1;
            int first = calcDist(start.prev, end, depth);
            if(start.prev2 != null) {
                int second = calcDist(start.prev2, end, depth);
                return Math.min(first, second);
            }
            return first;
        }

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
                Objects.equals(prev2, commit.prev2) &&
                Objects.equals(commitFile, commit.commitFile);
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
