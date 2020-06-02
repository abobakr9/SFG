import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

import java.lang.*;

public class sfgGUI extends JFrame {

    private JPanel mainPanel;
    private JButton enterButton;
    private JTextField numberField;
    private JTextField fromAddField;
    private JTextField toADDField;
    private JButton addNodeButton;
    private JTextField gainAddField;
    private JTextArea textArea1;
    private JTextField fromRemoveField;
    private JTextField toRemoveField;
    private JButton removeNodeButton;
    private JLabel numberlabel;
    private JLabel addlabel;
    private JLabel removelabel;
    private JLabel informationlabel;
    private JLabel mylabel;
    private JButton calculateButton;
    private JLabel tttlabel;
    private JTextArea textArea2;
    private JPanel labell;
    private JLabel plabel;
    private JPanel ttlabel;
    int flag1 =0;
    SignalFlowGraph g;
    Graph graph;
    SpriteManager sman;
    Sprite s;
    int v;
    ArrayList<Integer> flags;

    public sfgGUI(String title){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (flag1 == 1){
                    numberlabel.setText("not possible , you already entered "+v);
                    return;
                }
                String getValue = numberField.getText();
                if (getValue == null || getValue == "") {
                    numberlabel.setText("It is empty");
                    return;
                }
                try {
                    v = Integer.parseInt(getValue);
                    g = new SignalFlowGraph(v);
                    numberlabel.setText("Graph of "+v+" vetices has been done successfully");
                    flags=new ArrayList<Integer>();
                    int sss=v-1;
                    informationlabel.setText("you can enter nodes names from 0 to "+sss+" only");
                    plabel.setText("the i/p node will be 0 & the o/p node will be "+ sss);
                    flag1=1;
                    graph = new MultiGraph("SFG");
                    graph.addAttribute("ui.antialias"); //this does something...
                    graph.addAttribute("ui.quality"); //this sounds less important...
                    graph.setAttribute("ui.stylesheet", "node:clicked {\n" +
                            "    fill-color: purple;\n" +
                            "    text-size:    16;\n" +
                            "    text-style:   bold;\n" +
                            "    text-color:   #FFF;\n" +
                            "    text-alignment: at-right; \n" +
                            "    text-padding: 3px, 2px; \n" +
                            "    text-background-mode: rounded-box; \n" +
                            "    text-background-color: #A7CC; \n" +
                            "    text-color: white; \n" +
                            "    text-offset: 5px, 0px; \n" +
                            "}\n" +
                            "\n" +
                            "node {\n" +
                            "    size:         20px;\n" +
                            "    shape:        circle;\n" +
                            "    fill-color:   #8facb4;\n" +
                            "    stroke-mode:  plain;\n" +
                            "    stroke-color: black;\n" +
                            "\n" +
                            "}\n" +
                            "\n" +
                            "edge {\n" +
                            "    size:           1px;\n" +
                            "    fill-mode:      plain;\n" +
                            // "    shape:          angle;\n" +
                            "    text-size:      20px;\n" +
                            "}"); //get some style
                    System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
                     sman = new SpriteManager(graph);
                     s = sman.addSprite("s1");
                    graph.setStrict(false);
                    graph.setAutoCreate( true );
                } catch (NumberFormatException nfe) {
                    numberlabel.setText("Enter right input");
                    return ;
                }

            }
        });
        addNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mylabel.setText("");
                addlabel.setText("");
                String getto = toADDField.getText();
                String getfrom = fromAddField.getText();
                String getgain = gainAddField.getText();
                try {
                   int from  = Integer.parseInt(getfrom);
                    int to  = Integer.parseInt(getto);
                   // int gain  = d.parseInt(getgain);
                    double gain =Double.parseDouble(getgain);


                    if(from<v && to<v) {
                        if(graph.getNode(""+from) == null){
                            graph.addNode(""+from).addAttribute("ui.label",""+from);
                        }
                        if(graph.getNode(""+to) == null){
                            graph.addNode(""+to).addAttribute("ui.label",""+to);
                        }
                        if(g.getGain(from,to) != 0){
                            gain+= g.getGain(from,to);
                            g.removeEdge(from,to);
                            g.addEdge(from, to, gain);
                            graph.addEdge("" + from + "" + to, "" + from, "" + to, true).addAttribute("ui.label", "" + gain);
                            mylabel.setText("Edge from " + from + " to " + to + " has been updated successfully");
                        }
                        else {
                            g.addEdge(from, to, gain);
                            graph.addEdge("" + from + "" + to, "" + from, "" + to, true).addAttribute("ui.label", "" + gain);
                            mylabel.setText("Edge from " + from + " to " + to + " has been added successfully");
                        }
                    }
                    else{
                        mylabel.setText("you must enter nodes < "+ v);
                    }
                } catch (NumberFormatException nfe) {
                    addlabel.setText("Enter right input");
                    return ;
                }
            }
        });
        removeNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removelabel.setText("");
                String getto = toRemoveField.getText();
                String getfrom = fromRemoveField.getText();
                try {
                    int from  = Integer.parseInt(getfrom);
                    int to  = Integer.parseInt(getto);
                    if(from<v && to<v) {
                        g.removeEdge(from, to);
                        graph.removeEdge(""+from+""+to);
                        removelabel.setText("Edge from " + from + " to " + to + " has been removed successfully");
                    }
                    else{
                        removelabel.setText("you must enter nodes < "+ v);
                    }
                } catch (NumberFormatException nfe) {
                    removelabel.setText("Enter right input");
                    return ;
                }
            }
        });
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(g==null){
                    return;
                }
                int ss = 0, d = v-1;
                System.out.println("Print all paths from " + ss + " to " + d);
                g.printPaths(ss,d);
                List<List<List<Integer>>> mylist=g.nonTouchedLoops();
                List<List<Integer>> fp =g.getForwardPaths();
                List<List<Integer>> l =g.getLoops();
               double tf = g.Intialize();
               textArea1.setText("all forward paths: \n");
               for (int i =0 ;i<fp.size();i++){

                   for (int j=0;j<fp.get(i).size();j++){
                       if(j==0){
                           textArea1.append("path"+(i)+":  "+fp.get(i).get(j));
                       }
                       else{
                       textArea1.append(" -> "+fp.get(i).get(j));
                       }
                   }
                   textArea1.append("\n");
               }
                textArea1.append("All Loops:\n");
                for (int i =0 ;i<l.size()-1;i++){

                    for (int j=0;j<l.get(i).size();j++){
                        if(j ==0){
                            textArea1.append("L"+(i)+":  "+l.get(i).get(j));
                        }
                        else{
                            textArea1.append(" -> "+l.get(i).get(j));
                        }
                    }
                    textArea1.append("\n");
                }
                textArea1.append("Untouched loops :\n");
               for (int i=0;i<mylist.size()-1;i++){

                   for (int j=0 ;j<mylist.get(i).size();j++){
                       for(int k=0;k<mylist.get(i).get(j).size();k++){
                           textArea1.append("L"+mylist.get(i).get(j).get(k));
                       }
                       if(j != mylist.get(i).size()-1){
                           textArea1.append(" , ");
                       }
                   }
                   textArea1.append("\n");

               }
               tttlabel.setText("T.F = "+tf);
               textArea2.setText("Main Delta = "+g.bigDelata());
                double[] deltas =g.deltas();
                textArea2.append("\n\n");
                for(int i=0;i<deltas.length;i++){
                    textArea2.append("delta"+i+"=  "+deltas[i]+"\n");
                }
                graph.display();
            }
        });
    }

    public static void main(String[]args){
        JFrame frame = new sfgGUI("Signal Flow Graph");
        frame.setVisible(true);

    }


}