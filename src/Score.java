class Score implements Comparable<Score> {
    int killed;
    String time;

    public Score(int killed, String time) {
        this.killed = killed;
        this.time = time;
    }

    @Override
    public int compareTo(Score other) {
        // Compare scores based on enemies killed
        return Integer.compare(this.killed, other.killed);
    }
}
