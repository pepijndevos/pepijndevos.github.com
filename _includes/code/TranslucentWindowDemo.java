import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static java.awt.GraphicsDevice.WindowTranslucency.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAImage;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

public class TranslucentWindowDemo extends JFrame {
    private static String appid = "wolfram app id";
    private WAEngine engine = new WAEngine();
    private JLabel picLabel = new JLabel();

    public void requestWA(String input) {
        WAQuery query = engine.createQuery();
        query.setInput(input);
        try {
            // For educational purposes, print out the URL we are about to send:
            System.out.println("Query URL:");
            System.out.println(engine.toURL(query));
            System.out.println("");
            
            // This sends the URL to the Wolfram|Alpha server, gets the XML result
            // and parses it into an object hierarchy held by the WAQueryResult object.
            WAQueryResult queryResult = engine.performQuery(query);
            
            if (queryResult.isError()) {
                System.out.println("Query error");
                System.out.println("  error code: " + queryResult.getErrorCode());
                System.out.println("  error message: " + queryResult.getErrorMessage());
            } else if (!queryResult.isSuccess()) {
                System.out.println("Query was not understood; no results available.");
            } else {
                // Got a result.
                System.out.println("Successful query. Pods follow:\n");
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError() && !pod.getTitle().equals("Input") && !pod.getTitle().equals("Input interpretation")) {
                        System.out.println(pod.getTitle());
                        System.out.println("------------");
                        for (WASubpod subpod : pod.getSubpods()) {
                            for (Object element : subpod.getContents()) {
                                if (element instanceof WAImage) {
                                    WAImage waimg = (WAImage)element;
                                    waimg.acquireImage();
                                    System.out.println(waimg.getFile());
                                    System.out.println("");
                                    BufferedImage myPicture = ImageIO.read(waimg.getFile());
                                    picLabel.setIcon(new ImageIcon(myPicture));
                                    SwingUtilities.invokeLater(new Runnable() {
                                      public void run() {
                                        revalidate();
                                        repaint();
                                    }});
                                    return;
                                }
                            }
                        }
                        System.out.println("");
                    }
                }
                // We ignored many other types of Wolfram|Alpha output, such as warnings, assumptions, etc.
                // These can be obtained by methods of WAQueryResult or objects deeper in the hierarchy.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
      
    public TranslucentWindowDemo() {
        super("TranslucentWindow");
        setUndecorated(true);
        getRootPane().putClientProperty("Window.shadow", Boolean.FALSE);
        getContentPane().setBackground(Color.WHITE);

        setLayout(new FlowLayout());

        engine.setAppID(appid);
        engine.addFormat("image");
        //requestWA("integrate x^2");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                setOpacity(0.55f);
            }
            @Override
            public void mouseExited(MouseEvent me) {
                setOpacity(0.1f);
            }
        });

        setSize(300,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add a sample button.
        final JButton button = new JButton("Solve");
        final JTextField textfield = new JTextField(20);
        ActionListener submitListener = new ActionListener() {
          public void actionPerformed(ActionEvent ae){
            requestWA(textfield.getText());
          }
        };
        button.addActionListener(submitListener);
        textfield.addActionListener(submitListener);
        //add(button);
        add(textfield);
        add(picLabel);
    }

    public static void main(String[] args) {
        try {
        Thread.sleep(10000);
        } catch (InterruptedException e){}
        // Determine if the GraphicsDevice supports translucency.
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
	final Rectangle rect = gd.getDefaultConfiguration().getBounds();

        //If translucent windows aren't supported, exit.
        if (!gd.isWindowTranslucencySupported(TRANSLUCENT)) {
            System.err.println(
                "Translucency is not supported");
                System.exit(0);
        }
        
        //JFrame.setDefaultLookAndFeelDecorated(true);

        // Create the GUI on the event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TranslucentWindowDemo tw = new TranslucentWindowDemo();

                // Set the window to 55% opaque (45% translucent).
                tw.setOpacity(0.1f);
                tw.setAlwaysOnTop(true);

                // Display the window.
                //tw.pack();
		tw.setLocation((int)rect.getMaxX()-400,(int)rect.getMaxY()-300);
                tw.setVisible(true);
            }
        });
    }
}
