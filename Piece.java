public class Piece {

    private String color = "b";
    public String getStatus() {
        return color;
    }

    public void printColor() {
        System.out.print(this.color);
    }
    public void changeColor() {
        if (this.color.equals("w")) {
            this.color = "b";
        } else {
            this.color = "w";
        }
    }


}
