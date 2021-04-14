//������ ������ clustering�Ǵ��� ����
//-���� ��ǥ�� bfs�����Ͽ� �ٴ��� �������� boolean������ ����(bfs ����� ���� ��ǥ ��� ����Ʈ�� ����)
//--boolean���� �ٲ��� �ʴ´ٸ�(clustering�� false�� �����ȴٸ�) clustering�۾� ����. false�� ����Ʈ clear
//clustering ����->cluster�� �� �ٴ� ��ǥ�� ����. �� �ٴ��� x/�ٴ��� ���� �������� �ּ� count
//�ּ� count �������� ����Ʈ�� ����� ��� ������ x���� ����
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class B2933 {
	static boolean clustering=true;
	static int[][] board;
	static boolean[][] visited;
	static class XY{
		int x;
		int y;
		XY(int x, int y){
			this.x=x;
			this.y=y;
		}
	}
	static Queue<XY> queue= new LinkedList<XY>();
	static ArrayList<XY> clusterQueue=new ArrayList<XY>();
	static ArrayList<XY> clusterFloor=new ArrayList<XY>();
	static ArrayList<XY> clusterDir=new ArrayList<XY>();
	static StringBuilder sb=new StringBuilder();
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader input=new BufferedReader(new InputStreamReader(System.in));
		
		String[] s= input.readLine().split(" ");
		int r=Integer.parseInt(s[0]);
		int c=Integer.parseInt(s[1]);
		visited=new boolean[r+1][c+1];
		board=new int[r+1][c+1];
		for(int i=r;i>0;i--) {
			s=input.readLine().split("");
			for(int i1=0;i1<c;i1++) {
				if(s[i1].equals("."))board[i][i1+1]=0;
				else board[i][i1+1]=1;
				//�̳׶� ĭ�� 1, �� ĭ�� 0
			}
		}
		/*clusterQueue.add(new XY(1,2));
		clusterQueue.add(new XY(3,1));
		clusterQueue.add(new XY(4,4));
		clusterQueue.add(new XY(3,4));
		Collections.sort(clusterQueue);*/
		//�������� ����
		int rockCount=Integer.parseInt(input.readLine());
		s=input.readLine().split(" ");
		for(int i=0;i<rockCount;i++) {
			int x=Integer.parseInt(s[i]);
			if(i%2==0) {//���ʿ��� ����������
				for(int i1=1;i1<=c;i1++) {
					if(rockJudge(x,i1,r,c))
						break;
				}
			}
			else {//�����ʿ��� ��������
				for(int i1=c;i1>=1;i1--) {
					if(rockJudge(x,i1,r,c))
						break;
				}
			}
			//print(r,c);
		}
		print(r,c);
		input.close();
	}
	
	static void bfs(int x, int y, int r, int c) {
		int[] dx= {-1,0,0,1};
		int[] dy= {0,-1,1,0};
		while(!queue.isEmpty()) {
			XY temp=queue.poll();
			clusterQueue.add(temp);
			for(int i=0;i<4;i++) {
				int newX=temp.x+dx[i];
				int newY=temp.y+dy[i];
				if(newX>=1&&newX<=r&&newY>=1&&newY<=c&&!visited[newX][newY]&&board[newX][newY]==1) {//��ȿ�� �˻�
					if(newX==1) clustering=false;
					if(!clustering) return;
					visited[newX][newY]=true;
					queue.add(new XY(newX, newY));
				}
			}
		}
	}
	
	/*static void visitedClear(int r, int c) {
		for(int i=1;i<=r;i++) {
			for(int i1=1;i1<=c;i1++)
				visited[i][i1]=false;
		}
	}*/
	
	static boolean rockJudge(int x, int i1, int r, int c) {
		if(board[x][i1]==1) {
			board[x][i1]=0;
			visited[x][i1]=true;
			int[] dx= {-1,0,0,1};
			int[] dy= {0,-1,1,0};
			for(int i=0;i<4;i++) {
				int newX=x+dx[i];
				int newY=i1+dy[i];
				if(newX>=1&&newX<=r&&newY>=1&&newY<=c&&board[newX][newY]==1) {//��ȿ�� �˻�
					clusterDir.add(new XY(newX,newY));
				}
			}
			for(int i=0;i<clusterDir.size();i++) {
				queue.add(clusterDir.get(i));
				bfs(x,i1,r,c);
				if(clustering) {
					cluster();
					//�и� �۾� ���� �Լ� �߰�
				}
				else clustering=true;//�и����� ����
				queue.clear();
				clusterQueue.clear();
				clusterFloor.clear();
				visited=new boolean[r+1][c+1];
			}
			clusterDir.clear();
			return true;
		}
		return false;
	}
	
	static void cluster() {
		//findFloor(); �ٴ� ��ǥ ã�� �ʿ䰡 ������?
		for(int i=0;i<clusterQueue.size();i++)
			board[clusterQueue.get(i).x][clusterQueue.get(i).y]=0;
		int count=0;
		while(true) {
			count++;
			for(int i=0;i<clusterQueue.size();i++) {
				XY temp=clusterQueue.get(i);
				if(temp.x-count==1||(temp.x-count>=2&&board[temp.x-count-1][temp.y]==1)) {
					makingFloor(count);
					return;
				}
			}
		}
	}
	static void findFloor() {//�ٴ� ��ǥ ã��+���忡�� Ŭ������ ����(���߿� �ٽ� ���� ��)
		//Collections.sort(clusterQueue);
		for(int i=0;i<clusterQueue.size();i++) {
			XY temp=clusterQueue.get(i);
			board[temp.x][temp.y]=0;
			if(board[temp.x-1][temp.y]==0)
				clusterFloor.add(temp);
		}
	}
	
	static void makingFloor(int count) {
		for(int i=0;i<clusterQueue.size();i++)
			board[clusterQueue.get(i).x-count][clusterQueue.get(i).y]=1;
	}
	
	static void print(int r, int c) {
		for(int i=r;i>0;i--) {
			for(int i1=1;i1<=c;i1++) {
				if(board[i][i1]==0)sb.append(".");
				else sb.append("x");
			}
			sb.append("\n");
		}
		System.out.print(sb);
	}
}