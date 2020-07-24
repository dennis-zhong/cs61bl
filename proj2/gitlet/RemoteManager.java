package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

public class RemoteManager implements Serializable {
    HashMap<String, String> remotes;

    public RemoteManager() {
        remotes = new HashMap<>();
    }

    public String getPath(String name) {
        return remotes.get(name);
    }

    public void addRemote(String name, String path) {
        if(remotes.keySet().contains(name)) {
            System.out.println("A remote with that name already exists.");
        } else {
            remotes.put(name, path);
        }
    }

    public void removeRemote(String name) {
        if(remotes.keySet().contains(name)) {
            System.out.println("A remote with that name already exists.");
        } else {
            remotes.remove(name);
        }
    }

    public void saveRemoteFile() {
        Utils.writeObject(Main.REMOTES, this);
    }
}
