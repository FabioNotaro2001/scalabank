package scalabank.gui;

import scala.Tuple2;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.*;

class SwingFunctionalFacade {

    public interface Frame {
        String CLOSED = "CLOSED";

        Frame setSize(int width, int height);
        Frame setMinSize(int width, int height);
        Frame addView(String name, LayoutManager layout);
        Frame showView(String name);
        Frame addPanel(String name, LayoutManager layout, String panel, Object constraints);
        Frame addButton(String name, String text, String panel, Object constraints);
        Frame addLabel(String name, String text, String panel, Object constraints);
        Frame changeLabel(String name, String text);
        Frame addInput(String name, int columns, String panel, Object constraints);
        String getInputText(String name);
        Frame addComboBox(String name, String[] options, String panel, Object constraints);
        String getComboBoxSelection(String name);
        Frame show();
        Supplier<Tuple2<String, String>> events();
    }

    // TODO: change
    public static Frame createFrame(){
        return new FrameImpl();
    }

    private static class FrameImpl implements Frame {
        private final JFrame jframe = new JFrame();
        private final Map<String, JButton> buttons = new HashMap<>();
        private final Map<String, JLabel> labels = new HashMap<>();
        private final Map<String, JTextField> textFields = new HashMap<>();
        private final Map<String, JComboBox<String>> comboBoxes = new HashMap<>();
        private String currentView = "";
        private final Map<String, JPanel> views = new HashMap<>();
        private final Map<String, JPanel> panels = new HashMap<>();

        private final LinkedBlockingQueue<Tuple2<String, String>> eventQueue = new LinkedBlockingQueue<>();

        private final Supplier<Tuple2<String, String>> events = () -> {
            try{
                return eventQueue.take();
            } catch (InterruptedException e){
                return new Tuple2<>("", "");
            }
        };
        public FrameImpl() {
            this.jframe.setLayout(new FlowLayout());
            this.jframe.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e){
                    try {
                        eventQueue.put(new Tuple2<>(Frame.CLOSED, ""));
                    } catch (InterruptedException ex){}
                }
            });
        }

        @Override
        public Frame setSize(int width, int height) {
            this.jframe.setSize(width, height);
            return this;
        }

        @Override
        public Frame setMinSize(int width, int height) {
            this.jframe.setMinimumSize(new Dimension(width, height));
            return this;
        }

        @Override
        public Frame addButton(String name, String text, String panel, Object constraints) {
            JButton jb = new JButton(text);
            jb.setActionCommand(name);
            this.buttons.put(name, jb);
            jb.addActionListener(e -> {
                try {
                    eventQueue.put(new Tuple2<>(name, ""));
                } catch (InterruptedException ex){}
            });
            this.panels.get(panel).add(jb, constraints);
            return this;
        }

        @Override
        public Frame addLabel(String name, String text, String panel, Object constraints) {
            JLabel jl = new JLabel(text);
            this.labels.put(name, jl);
            this.panels.get(panel).add(jl, constraints);
            return this;
        }

        @Override
        public Frame addInput(String name, int columns, String panel, Object constraints) {
            JTextField jt = new JTextField("", columns);
            this.textFields.put(name, jt);
/*            jt.getDocument().addDocumentListener(new DocumentListener() {
                private void createEvent() {
                    try {
                        eventQueue.put(new Tuple2<>(name, jt.getText()));
                    } catch (InterruptedException ex){}
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    createEvent();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    createEvent();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    createEvent();
                }
            });*/
            this.panels.get(panel).add(jt, constraints);
            return this;
        }

        @Override
        public String getInputText(String name) {
            return this.textFields.get(name).getText();
        }

        @Override
        public Frame addComboBox(String name, String[] options, String panel, Object constraints) {
            JComboBox<String> jc = new JComboBox<>(options);
            this.comboBoxes.put(name, jc);
/*            jc.addActionListener(e -> {
                try {
                    Object selected = jc.getSelectedItem();
                    if (selected != null) {
                        eventQueue.put(new Tuple2<>(name, (String) selected));
                    }
                } catch (InterruptedException ex){}
            });*/
            this.panels.get(panel).add(jc, constraints);
            return this;
        }

        @Override
        public String getComboBoxSelection(String name) {
            return (String) this.comboBoxes.get(name).getSelectedItem();
        }

        @Override
        public Frame addView(String name, LayoutManager layout) {
            JPanel jp = new JPanel(layout);
            jp.setVisible(false);
            this.views.put(name, jp);
            this.panels.put(name, jp);
            this.jframe.getContentPane().add(jp);
            return this;
        }

        @Override
        public Frame showView(String name) {
            if (!this.currentView.isEmpty()) {
                this.views.get(currentView).setVisible(false);
            }
            this.currentView = name;
            this.views.get(currentView).setVisible(true);
            this.jframe.pack();
            return this;
        }

        @Override
        public Frame addPanel(String name, LayoutManager layout, String panel, Object constraints) {
            JPanel jp = new JPanel(layout);
            this.panels.put(name, jp);
            this.panels.get(panel).add(jp, constraints);
            jp.setVisible(true);
            return this;
        }

        @Override
        public Supplier<Tuple2<String, String>> events() {
            return events;
        }

        @Override
        public Frame changeLabel(String name, String text) {
            this.labels.get(name).setText(text);
            return this;
        }

        @Override
        public Frame show() {
            this.jframe.setVisible(true);
            return this;
        }

    }
}
