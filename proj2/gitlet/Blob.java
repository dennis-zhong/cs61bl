package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

public class Blob implements Serializable {
    String name;
    String contents;
    File blobFile;

    public Blob() {}

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

    public String getBlobID() {
        return this.blobFile.getName();
    }

    public static Blob getBlobObj(String name) {
        File blobFile = new File(".gitlet/blobs/"+name);
        if(blobFile.exists()) {
            return Utils.readObject(blobFile, Blob.class);
        } else {
            return new Blob();
        }
    }

    public boolean isEmpty() {
        return this.equals(new Blob());
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
        Utils.writeContents(new File(name), contents);
        this.saveBlob();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blob blob = (Blob) o;
        return Objects.equals(name, blob.name) &&
                Objects.equals(contents, blob.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, contents, blobFile);
    }

    @Override
    public String toString() {
        return Utils.sha1(Utils.serialize(this));
    }
}
