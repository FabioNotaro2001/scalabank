package scalabank.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

class SwingFunctionalFacade {

    public static interface Frame {
        public static String CLOSED = "CLOSED";

        Frame setSize(int width, int height);
        Frame addView(String name, LayoutManager layout);
        Frame showView(String name);
        Frame addPanel(String name, LayoutManager layout, String view, Object constraints);
        Frame addButton(String name, String text, String view, Object constraints);
        Frame addLabel(String name, String text, String view, Object constraints);
        Frame changeLabel(String name, String text);
        Frame show();
        Supplier<String> events();        
    }

    // TODO: change
    public static Frame createFrame(){
        return new FrameImpl();
    }

    private static class FrameImpl implements Frame {
        private final JFrame jframe = new JFrame();
        private final Map<String, JButton> buttons = new HashMap<>();
        private final Map<String, JLabel> labels = new HashMap<>();
        private String currentView = "";
        private final Map<String, JPanel> views = new HashMap<>();
        private final Map<String, JPanel> panels = new HashMap<>();

        private final LinkedBlockingQueue<String> eventQueue = new LinkedBlockingQueue<>();

        private final Supplier<String> events = () -> {
            try{
                return eventQueue.take();
            } catch (InterruptedException e){
                return "";
            }
        };
        public FrameImpl() {
            this.jframe.setLayout(new FlowLayout());

            this.jframe.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e){
                    try {
                        eventQueue.put(Frame.CLOSED);
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
        public Frame addButton(String name, String text, String panel, Object constraints) {
            JButton jb = new JButton(text);
            jb.setActionCommand(name);
            this.buttons.put(name, jb);
            jb.addActionListener(e -> {
                try {
                    eventQueue.put(name);
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
        public Supplier<String> events() {
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
