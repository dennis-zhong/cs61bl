package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

public class Stage implements Serializable {
    HashMap<String, String> blobs;
    String name;

    public Stage(String name) {
        blobs = new HashMap<>();
        this.name = name;
    }

    public HashMap<String, String> getBlobs() {
        return blobs;
    }

    public void putOnStage(Blob blob) {
        blobs.put(blob.getName(), blob.getBlobFile().getName());
        this.saveStage();
    }

    public void unStage(Blob blob) {
        blobs.remove(blob.getName());
        this.saveStage();
    }

    public boolean isEmpty() {
        return blobs.isEmpty();
    }

    public void saveStage() {
        Utils.writeObject(new File(".gitlet/"+name), this);
    }

    public void emptyStage() {
        blobs = new HashMap<>();
        this.saveStage();
    }
}
