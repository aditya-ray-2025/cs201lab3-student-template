public class Q1Test {
     public static void main(String[] args) {

          String result, traversal1, traversal2, traversal3;

          traversal1 = "1-2";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "3-2-1";
          traversal3 = "2-3-1";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "3-1-2-5-4";
          traversal2 = "2-1-4-5-3";
          traversal3 = "1-2-3-4-5";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Preorder, Traversal 2 - Postorder, Traversal 3 - Inorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "10-20-30-40-50";
          traversal2 = "20-10-30-40-50";
          traversal3 = "10-50-40-30-20";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();
     } 

      // write your codes here
     public static String verify(String traversal1, String traversal2, String traversal3) {
          String[] raw = { traversal1, traversal2, traversal3 };
          int[][] t = new int[3][];
          for (int i = 0; i < 3; i++) {
               String[] parts = raw[i].split("-");
               t[i] = new int[parts.length];
               for (int j = 0; j < parts.length; j++) {
                    t[i][j] = Integer.parseInt(parts[j].trim());
               }
          }

          int n = t[0].length;
          if (t[1].length != n || t[2].length != n) {
               return "Invalid traversals";
          }

          int[][] perms = { {0,1,2}, {0,2,1}, {1,0,2}, {1,2,0}, {2,0,1}, {2,1,0} };

          for (int[] p : perms) {
               int[] in = t[p[0]], pre = t[p[1]], post = t[p[2]];

               boolean sorted = true;
               for (int i = 1; i < n; i++) {
                    if (in[i] <= in[i - 1]) { sorted = false; break; }
               }
               if (!sorted) {
                    continue;
               }

               int[] left = new int[n], right = new int[n];
               for (int i = 0; i < n; i++) { left[i] = -1; right[i] = -1; }
               for (int i = 1; i < n; i++) {
                    int cur = 0;
                    while (true) {
                         if (pre[i] < pre[cur]) {
                              if (left[cur] == -1) { left[cur] = i; break; }
                              cur = left[cur];
                         } else {
                              if (right[cur] == -1) { right[cur] = i; break; }
                              cur = right[cur];
                         }
                    }
               }

               int[] stack = new int[n + 1];
               int sp, k;

               // preorder of rebuilt tree
               int[] gotPre = new int[n];
               sp = 0; k = 0;
               stack[sp++] = 0;
               while (sp > 0) {
                    int cur = stack[--sp];
                    gotPre[k++] = pre[cur];
                    if (right[cur] != -1) stack[sp++] = right[cur];
                    if (left[cur] != -1) stack[sp++] = left[cur];
               }

               // inorder of rebuilt tree
               int[] gotIn = new int[n];
               sp = 0; k = 0;
               int cur = 0;
               while (cur != -1 || sp > 0) {
                    while (cur != -1) { stack[sp++] = cur; cur = left[cur]; }
                    cur = stack[--sp];
                    gotIn[k++] = pre[cur];
                    cur = right[cur];
               }
               int[] gotPost = new int[n];
               sp = 0; k = n - 1;
               stack[sp++] = 0;
               while (sp > 0) {
                    int c = stack[--sp];
                    gotPost[k--] = pre[c];
                    if (left[c] != -1) stack[sp++] = left[c];
                    if (right[c] != -1) stack[sp++] = right[c];
               }

               boolean match = true;
               for (int i = 0; i < n; i++) {
                    if (gotPre[i] != pre[i] || gotIn[i] != in[i] || gotPost[i] != post[i]) {
                         match = false;
                         break;
                    }
               }

               if (match) {
                    String[] names = new String[3];
                    names[p[0]] = "Inorder";
                    names[p[1]] = "Preorder";
                    names[p[2]] = "Postorder";
                    return "Traversal 1 - " + names[0] + ", Traversal 2 - " + names[1]
                         + ", Traversal 3 - " + names[2];
               }
          }
          return "Invalid traversals";
     }
}
