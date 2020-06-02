import org.graphstream.graph.*;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import java.util.*;
import java.lang.*;
import java.io.*;
public class SignalFlowGraph <T extends Comparable<T>> {
    private int vNumber;
    private ArrayList[] adj;
    List<List<Integer>> forwardPaths;
    List<List<List<Integer>>> editing=new ArrayList<>();
    int pathNumber ;
    List<List<Integer>> Loops= new ArrayList<>();
    int loop_index=0;
    double[][]weights;
    public SignalFlowGraph(int V){
        this.vNumber = V;
        adj = new ArrayList[V];
        forwardPaths= new ArrayList<>();
        weights=new double[V][V];
        pathNumber=0;
        for(int i=0; i<V; i++)
            adj[i] = new ArrayList<Integer>();
    }
    public List<List<Integer>> getForwardPaths(){
        return forwardPaths;
    }
    public List<List<Integer>> getLoops(){
        return Loops;
    }

    public void addEdge(int v, int w,double weigh){
        adj[v].add(w);
        weights[v][w]=weigh;
    }
    public void removeEdge(int v, int w){
        for (int i=0;i<adj[v].size();i++){
            if(adj[v].get(i) == (Integer)w){
                adj[v].remove(i);
                weights[v][w]=0;
            }
        }
    }
    public double getGain(int i,int j){
        return weights[i][j];
    }



    public void printPaths(int s, int d){
        Boolean[] visited = new Boolean[vNumber];
        int path_index=0;
        forwardPaths.add(new ArrayList<>());
        Loops.add(new ArrayList<>());
        for(int i=0; i<vNumber; i++){
            visited[i] =false;
        }

        printPathsUtil(s,d,visited,path_index);
        if(forwardPaths.size()>0){
            forwardPaths.remove(forwardPaths.size()-1);
        }
    }

    public void printPathsUtil(int s, int d, Boolean[] visited,int path_index){
        visited[s] = true;
        forwardPaths.get(pathNumber).add(path_index,s);
        path_index++;

        if(s==d){
           ArrayList<Integer> list=new ArrayList<>();
            for(int i=0; i<path_index; i++) {
                System.out.print(forwardPaths.get(pathNumber).get(i) + " ");
                list.add(forwardPaths.get(pathNumber).get(i));
            }
            System.out.println();
            pathNumber++;
            forwardPaths.add(list);

        }
        else{
            ArrayList<Integer> list = adj[s];
            boolean flag=false;
            for(Integer i: list){
                if(visited[i]){
                    ArrayList<Integer> mylist=new ArrayList<>();
                    for (int j=0;j<forwardPaths.get(pathNumber).size();j++){
                        if(!flag) {
                            if (i == forwardPaths.get(pathNumber).get(j)) {
                                flag = true;
                            }
                        }
                        if(flag) {
                            mylist.add(forwardPaths.get(pathNumber).get(j));
                            //Loops.get(loop_index).add(forwardPaths.get(pathNumber).get(j));
                        }
                    }
                    flag=false;
                    mylist.add(i);
                  //  Loops.get(loop_index).add(i);
                    if(!Loops.contains(mylist)){
                        Loops.get(loop_index).addAll(mylist);
                        loop_index++;
                        Loops.add(new ArrayList<>());
                    }

                    mylist.clear();
                }
                if(!visited[i])
                    printPathsUtil(i,d,visited,path_index);
            }
        }
        path_index--;
        forwardPaths.get(pathNumber).remove(path_index);
        visited[s]=false;
    }
    public void printLoops() {
        for (int i = 0; i < Loops.size(); i++) {
            for (int j = 0; j < Loops.get(i).size(); j++) {
                System.out.print(Loops.get(i).get(j));
            }
            System.out.println(" ");
        }
    }
    public List<List<List<Integer>>> nonTouchedLoops(){
        List<List<List<Integer>>> myList=new ArrayList<>();
        myList.add(new ArrayList<>());
        int ii=0;
        for (int i=0 ;i<Loops.size()-2;i++){
            for(int j=i+1;j<Loops.size()-1;j++){
                boolean flag =true;
                for (int a=0;a<Loops.get(i).size();a++){
                    if(Loops.get(j).contains(Loops.get(i).get(a))){
                        flag=false;
                        break;
                    }
                }
                if(flag){
                    myList.get(0).add(new ArrayList<>());
                    myList.get(0).get(ii).add(i);
                    myList.get(0).get(ii).add(j);
                    ii++;
                }
            }

        }
        int n=0;
        while(myList.get(n).size()!=0){
            myList.add(new ArrayList<>());
            for(int i=0;i<myList.get(n).size();i++) {
                for (int j=0;j<Loops.size()-1;j++){
                    boolean flag =true;
                    ArrayList <Integer>smalllist =new ArrayList<>();
                    for (int a=0;a<myList.get(n).get(i).size();a++){
                          int tmp = myList.get(n).get(i).get(a);
                        for (int b=0;b<Loops.get(tmp).size();b++){
                            if(Loops.get(j).contains(Loops.get(tmp).get(b))){
                                flag=false;
                                break;
                            }
                        }
                        if (flag){
                            smalllist.add(myList.get(n).get(i).get(a));
                        }
                    }
                    if(!flag){
                        smalllist.clear();
                    }
                    else if (flag){
                        smalllist.add(j);
                        sortFast((ArrayList<T>) smalllist);
                        if(!myList.get(n+1).contains(smalllist))
                       myList.get(n+1).add(smalllist);
                    }
                }
            }
         n++;
        }
   editing=myList;
    return myList ;
    }
    public double gain(List<Integer>list){
        if (list.size()==0){
            return 0;
        }
        double s=1;
        for(int i=0;i<list.size()-1;i++){
            s=s*weights[list.get(i)][list.get(i+1)];
        }
    return s;
    }
    public double bigDelata(){
        double delta=1;
        double tmp=0;
        for (int i=0;i<Loops.size()-1;i++){
            tmp=tmp+gain(Loops.get(i));
        }
        delta-=tmp;
        List<List<List<Integer>>> mylist=nonTouchedLoops();
        if(mylist.size() ==0){
            return delta;
        }
        int flag=-1;
        for (int i=0;i<mylist.size()-1;i++){
            flag*=(-1);
            double tmp1=0;
            for (int j=0;j<mylist.get(i).size();j++){
                double m=1;
                for(int a=0;a<mylist.get(i).get(j).size();a++){
                    m*=gain(Loops.get(mylist.get(i).get(j).get(a)));
                }
                tmp1=tmp1+m;
            }
            delta+=(flag*tmp1);

        }
        return delta;
    }
    public double [] deltas(){
        double [] deltas =new double[forwardPaths.size()];
        for (int i=0;i<forwardPaths.size();i++){
            double tmp=0;
            List<Integer> non= new ArrayList<>();
            for (int j=0;j<Loops.size()-1;j++){
                boolean flag =true;
                for (int a=0;a<Loops.get(j).size();a++){
                    if(forwardPaths.get(i).contains(Loops.get(j).get(a))){
                        flag=false;
                        break;
                    }
                }
                if(flag) {
                    tmp += gain(Loops.get(j));
                    non.add(j);
                }

            }
            deltas[i]=1-tmp;
            deltas[i]= editGain(non,deltas[i]);
        }
        return deltas;
    }

    public double editGain(List<Integer> non,double deltta){
        if(non.isEmpty()){
            return deltta;
        }
        int flag=-1;
        for (int i=0;i<editing.size()-1;i++){
            if((non.size()-2)<0){
                break;
            }
            flag*=(-1);
            double tmp1=0;
            for (int j=0;j<editing.get(i).size();j++){
                boolean flag2=true;
                double m=1;
                for(int a=0;a<editing.get(i).get(j).size();a++){
                    if (!non.contains(editing.get(i).get(j).get(a))){
                        flag2=false;
                    }
                    m*=gain(Loops.get(editing.get(i).get(j).get(a)));
                }
                if(flag2)
                tmp1=tmp1+m;
            }
            deltta+=(flag*tmp1);

        }




    return deltta;
    }
    public void sortFast(ArrayList<T> unordered) {
        if (unordered != null && unordered.size() != 0) {
            mergeSort(0, unordered.size() - 1, unordered, new ArrayList<T>(unordered));        }
    }
    private void mergeSort(int low, int high, ArrayList<T> values, ArrayList<T> aux) {

        if(low < high){
            int mid = low + (high - low) / 2;
            mergeSort(low, mid, values, aux);
            mergeSort(mid+1, high, values, aux);
            merge(low, mid, high, values, aux);
        }
    }

    private void merge(int low, int mid, int high, ArrayList<T> values, ArrayList<T> aux) {

        int left = low;
        int right = mid + 1;

        for(int i = low; i <= high; i++){
            aux.set(i, values.get(i));
        }

        while(left <= mid && right <= high){
            values.set(low++, aux.get(left).compareTo(aux.get(right)) < 0 ? aux.get(left++) : aux.get(right++));
        }

        while(left <= mid){
            values.set(low++, aux.get(left++));
        }
    }
    public double Intialize(){
       double delta = bigDelata();
       double []deltas =deltas();
       double sum =0;
       for (int i=0 ;i<deltas.length;i++){
           sum+= (gain(forwardPaths.get(i))*deltas[i])/delta;
       }
       System.out.print("sum=");
       return sum;
    }

    }

