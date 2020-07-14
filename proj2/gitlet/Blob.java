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
        Utils.writeObject(blobFile, this);
    }

    public String getName() {
        return name;
    }

    public File getBlobFile() {
        return this.blobFile;
    }

    public String getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return Utils.sha1(Utils.serialize(this));
    }
}
