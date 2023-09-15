class FlyingEnemyBullet {
    private int x;
    private int y;
    private int speed;

    public FlyingEnemyBullet(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move() {
        x -= speed;
    }
}
