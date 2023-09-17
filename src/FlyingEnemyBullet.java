class FlyingEnemyBullet {
    private int x;
    private int y;
    private int speed=10;

    public FlyingEnemyBullet(int x, int y,int elapsedtime) {
        this.x = x;
        this.y = y;
        if(elapsedtime<250)
        {
            this.speed = speed;
        }
        else if (elapsedtime>250&elapsedtime<=450)
        {
            this.speed = (speed+4);
        }
        else if (elapsedtime>450)
        {
            this.speed =(speed+7) ;
        }
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
