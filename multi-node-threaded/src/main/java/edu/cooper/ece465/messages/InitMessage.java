package edu.cooper.ece465.messages;

import java.io.Serializable;

public class InitMessage implements Serializable {
    private final int startNode, endNode;

    public String getFilename() {
        return filename;
    }

    public byte[] getMd5hash() {
        return md5hash;
    }

    private String filename;
    private byte[] md5hash;

    public InitMessage(int startNode, int endNode, String filename, byte[] md5hash) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.filename = filename;
        this.md5hash = md5hash;
    }


    public int getStartNode() {
        return startNode;
    }

    public int getEndNode() {
        return endNode;
    }

}
