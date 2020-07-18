package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    String name;
    String contents;
    File blobFile;

    public Blob(String name) {
        this.name = name;
        contents = Utils.readContentsAsString(new File(name));
        blobFile = new File(".gitlet/blobs/"+toString());
    }

    public String getName() {
        return name;
    }

    public void saveBlob() {
        Utils.writeObject(blobFile, this);
    }

    public File getBlobFile() {
        return this.blobFile;
    }

    public static Blob getBlobObj(String name) {
        if(name == null) {
            return null;
        }
        return Utils.readObject(new File(".gitlet/blobs/"+name), Blob.class);
    }

    public String getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return Utils.sha1(Utils.serialize(this));
    }
}
