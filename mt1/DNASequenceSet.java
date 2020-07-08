public class DNASequenceSet {
    private DNANode sentinel;

    public DNASequenceSet() {
        sentinel = new DNANode(-1, false);
    }

    public void add(int[] sequence) {
        DNANode pointer = sentinel;
        for (int i = 0; i < sequence.length; i++) {
            if(pointer.nexts[sequence[i]].endOfSequence) { continue; }
            if (pointer.nexts[sequence[i]] == null) {
                pointer.nexts[sequence[i]] = new DNANode(sequence[i], false);
            }
            pointer = pointer.nexts[sequence[i]];
        }
        pointer.endOfSequence = true;
    }

    public boolean contains(int[] sequence) {
        DNANode pointer = sentinel;
        for (int i = 0; i < sequence.length; i++) {
            if(pointer.nexts[sequence[i]].endOfSequence) { return true; }
            if (pointer.nexts[sequence[i]] == null) {
                return false;
            }
            pointer = pointer.nexts[sequence[i]];
        }
        return false;
    }


    public static class DNANode {
        private int base;
        private boolean endOfSequence;
        private DNANode[] nexts;

        public DNANode(int b, boolean end) {
            this.base = b;
            this.endOfSequence = end;
            this.nexts = new DNANode[4];
        }
    }
}