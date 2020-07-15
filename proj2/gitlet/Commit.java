package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Commit implements Serializable {
    Date timestamp;
    String message;
    HashMap<String, Blob> blobs = new HashMap<>();
    Commit prev;
    File commitFile;

    public Commit() {
        timestamp = new Date(0);
        message = "initial commit";
        commitFile = new File(".gitlet/commit/"+this.toString());
    }

    public Commit(List<Blob> blobs, String message, Commit prev) {
        timestamp = new Date();
        this.message = message;
        for(Blob blob: blobs) {
            this.blobs.put(blob.name, blob);
        }
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

    public HashMap<String, Blob> getBlobs() {
        return blobs;
    }

    public Commit readCommit(String str) {
        return new Commit();
    }

    @Override
    public String toString() {
        return Utils.sha1(Utils.serialize(this));
    }
}
