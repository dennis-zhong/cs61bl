public class TriangleDrawer2{
  public static void main(String[] args){
    int SIZE = 10;
    for(int row = 0; row < SIZE; row = row + 1) {
      for(int col = 0; col < row; col = col + 1) {
        System.out.print('*');
      }
      System.out.println('*');
    }
  }
}
