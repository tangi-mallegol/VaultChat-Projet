/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controle.AbriLocalInterface;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import modele.Annuaire;
import modele.Abri;
import modele.Message;

/**
 *
 * @author Gwenole Lecorve
 * @author David Guennec
 */
public class AbriVue extends JFrame implements Observer {

    private AbriLocalInterface backend;
    private boolean emettreEnBoucle;

    /**
     * Creates new form AbriFenetre
     *
     * @param backend Traitements en arri�re-plan
     * @param abri Abri
     */
    public AbriVue(AbriLocalInterface backend, Abri abri) {
        this.backend = backend;
        this.emettreEnBoucle = false;

        initComponents();

        setResizable(false);
        setTitle("Abri - " + backend.getUrl());
        urlTextField.setText(backend.getUrl());
        etatLabel.setText("<html><a color=red>D�connect�</a></html>");

        emettreBouton.setEnabled(false);
        demarrerToggleBouton.setEnabled(false);
        stopperBouton.setEnabled(false);
        destinataireList.setEnabled(false);

        DefaultCaret caret = (DefaultCaret) receptionTextPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        abri.addObserver(this);
        backend.getAnnuaire().addObserver(this);

        abri.definirGroupe((String) groupsList.getSelectedValue());
        
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        receptionPanel = new javax.swing.JPanel();
        receptionScrollPane = new javax.swing.JScrollPane();
        receptionTextPane = new javax.swing.JTextPane();
        emissionPanel = new javax.swing.JPanel();
        emettreBouton = new javax.swing.JButton();
        emissionScrollPane = new javax.swing.JScrollPane();
        emissionTextArea = new javax.swing.JTextArea();
        listeScrollPane = new javax.swing.JScrollPane();
        destinataireList = new javax.swing.JList();
        destinataireList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        demarrerToggleBouton = new javax.swing.JToggleButton();
        stopperBouton = new javax.swing.JButton();
        statutPanel = new javax.swing.JPanel();
        etatLabel = new javax.swing.JLabel();
        connectionBouton = new javax.swing.JButton();
        urlTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        groupsList = new javax.swing.JList();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Abri");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        receptionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "R�ception", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        receptionTextPane.setContentType("text/html"); // NOI18N
        receptionTextPane.setDocument(new HTMLDocument());
        receptionScrollPane.setViewportView(receptionTextPane);

        javax.swing.GroupLayout receptionPanelLayout = new javax.swing.GroupLayout(receptionPanel);
        receptionPanel.setLayout(receptionPanelLayout);
        receptionPanelLayout.setHorizontalGroup(
            receptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(receptionScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        receptionPanelLayout.setVerticalGroup(
            receptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(receptionScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
        );

        emissionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "�mission", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        emettreBouton.setText("�mettre une fois");
        emettreBouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emettreBoutonActionPerformed(evt);
            }
        });

        emissionTextArea.setColumns(20);
        emissionTextArea.setRows(5);
        emissionTextArea.setText("Message (ex : Attaque de radcafards)");
        emissionTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                emissionTextAreaKeyReleased(evt);
            }
        });
        emissionScrollPane.setViewportView(emissionTextArea);

        destinataireList.setModel(new SortedListModel());
        destinataireList.setEnabled(false);
        listeScrollPane.setViewportView(destinataireList);

        demarrerToggleBouton.setText("D�marrer l'�mission");
        demarrerToggleBouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                demarrerToggleBoutonActionPerformed(evt);
            }
        });

        stopperBouton.setText("Stopper l'�mission");
        stopperBouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopperBoutonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout emissionPanelLayout = new javax.swing.GroupLayout(emissionPanel);
        emissionPanel.setLayout(emissionPanelLayout);
        emissionPanelLayout.setHorizontalGroup(
            emissionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(emissionPanelLayout.createSequentialGroup()
                .addComponent(listeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(emissionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(emettreBouton, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                    .addComponent(demarrerToggleBouton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stopperBouton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(emissionScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
        );
        emissionPanelLayout.setVerticalGroup(
            emissionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emissionPanelLayout.createSequentialGroup()
                .addComponent(emissionScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(emissionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(emissionPanelLayout.createSequentialGroup()
                        .addComponent(emettreBouton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(demarrerToggleBouton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopperBouton))
                    .addComponent(listeScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        statutPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Statut", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        etatLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etatLabel.setText("Non connect�");

        connectionBouton.setText("Connecter l'abri au r�seau");
        connectionBouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectionBoutonActionPerformed(evt);
            }
        });

        urlTextField.setEditable(false);
        urlTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        urlTextField.setText("rmi://url");

        groupsList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Groupe 1", "Groupe 2" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        groupsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        groupsList.setToolTipText("");
        groupsList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        groupsList.setSelectedIndex(0);
        jScrollPane1.setViewportView(groupsList);

        jSeparator1.setToolTipText("Groupe de danger");
        jSeparator1.setName("Groupe de dangers"); // NOI18N

        javax.swing.GroupLayout statutPanelLayout = new javax.swing.GroupLayout(statutPanel);
        statutPanel.setLayout(statutPanelLayout);
        statutPanelLayout.setHorizontalGroup(
            statutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane1)
                    .addComponent(urlTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statutPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(etatLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(connectionBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        statutPanelLayout.setVerticalGroup(
            statutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statutPanelLayout.createSequentialGroup()
                .addComponent(urlTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(statutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(etatLabel)
                    .addComponent(connectionBouton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(receptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(emissionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(statutPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(statutPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(receptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emissionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        receptionPanel.getAccessibleContext().setAccessibleName("Messages re�us");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void emettreBoutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emettreBoutonActionPerformed
        try {
            backend.emettreMessage(emissionTextArea.getText());
        } catch (Exception ex) {
            ex.printStackTrace();
            afficherErreur("Erreur lors de l'emission du message", ex.getMessage());
        }
    }//GEN-LAST:event_emettreBoutonActionPerformed

    private void demarrerToggleBoutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_demarrerToggleBoutonActionPerformed
        emettreEnBoucle = true;
        Thread t;
        t = new EmissionThread(this);
        t.start();
        ajusterEtatBoutons();
    }//GEN-LAST:event_demarrerToggleBoutonActionPerformed

    private void stopperBoutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopperBoutonActionPerformed
        emettreEnBoucle = false;
        ajusterEtatBoutons();
    }//GEN-LAST:event_stopperBoutonActionPerformed

    private void emissionTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emissionTextAreaKeyReleased
        if (emissionTextArea.getText().equals("")) {
            afficherErreur("Arret de l'emission", "L'envoi en boucle d'un message a ete interrompu en raison d'un message vide.");
            emettreEnBoucle = false;
        }
        ajusterEtatBoutons();
    }//GEN-LAST:event_emissionTextAreaKeyReleased

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            backend.deconnecterAbri();
        } catch (Exception ex) {
            ex.printStackTrace();
            afficherErreur("Erreur lors de la fermeture", ex.getMessage());
        }
    }//GEN-LAST:event_formWindowClosing

    private void connectionBoutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectionBoutonActionPerformed
        if (backend.estConnecte()) {
            try {
                backend.deconnecterAbri();
                etatLabel.setText("<html><a color=red>D�connect�</a></html>");
                connectionBouton.setText("Connecter l'abri au r�seau");
                if (emettreEnBoucle) {
                    emettreEnBoucle = false;
                    afficherErreur("Arret de l'emission", "L'envoi en boucle d'un message a ete interrompu en raison d'une deconnexion.");
                }
                ajusterEtatBoutons();
            } catch (Exception ex) {
                ex.printStackTrace();
                afficherErreur("Erreur de d�connexion", ex.getMessage());
            }
        } else {
            try {
                backend.changerGroupe((String) groupsList.getSelectedValue());
                backend.connecterAbri();
                etatLabel.setText("<html><a color=green>Connect�</a></html>");
                connectionBouton.setText("D�connecter l'abri du r�seau");
                ajusterEtatBoutons();
            } catch (Exception ex) {
                ex.printStackTrace();
                afficherErreur("Erreur de d�connexion", ex.getMessage());
            }
        }
    }//GEN-LAST:event_connectionBoutonActionPerformed

    protected void ajusterEtatBoutons() {
        // Non connecte ou message a envoyer vide ou aucun destinataire existant
        if (!backend.estConnecte() || emissionTextArea.getText().equals("") || destinataireList.getModel().getSize() == 0) {
            emettreBouton.setEnabled(false);
            demarrerToggleBouton.setSelected(false);
            demarrerToggleBouton.setEnabled(false);
            stopperBouton.setEnabled(false);
            destinataireList.setEnabled(false);
        } // Connecte et message a envoyer non vide et au moins un destinataire et envoi en boucle actif
        else if (emettreEnBoucle) {
            emettreBouton.setEnabled(false);
            demarrerToggleBouton.setSelected(true);
            demarrerToggleBouton.setEnabled(false);
            stopperBouton.setEnabled(true);
            destinataireList.setEnabled(false);
        } // Connecte et message a envoyer non vide et au moins un destinataire et envoi en boucle non actif
        else {
            emettreBouton.setEnabled(true);
            demarrerToggleBouton.setSelected(false);
            demarrerToggleBouton.setEnabled(true);
            stopperBouton.setEnabled(false);
            destinataireList.setEnabled(true);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectionBouton;
    private javax.swing.JToggleButton demarrerToggleBouton;
    private javax.swing.JList destinataireList;
    private javax.swing.JButton emettreBouton;
    private javax.swing.JPanel emissionPanel;
    private javax.swing.JScrollPane emissionScrollPane;
    private javax.swing.JTextArea emissionTextArea;
    private javax.swing.JLabel etatLabel;
    private javax.swing.JList groupsList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JScrollPane listeScrollPane;
    private javax.swing.JPanel receptionPanel;
    private javax.swing.JScrollPane receptionScrollPane;
    private javax.swing.JTextPane receptionTextPane;
    private javax.swing.JPanel statutPanel;
    private javax.swing.JButton stopperBouton;
    private javax.swing.JTextField urlTextField;
    // End of variables declaration//GEN-END:variables

    protected void remplirListe(Annuaire annuaire) {

        ArrayList<String> selection = new ArrayList<String>();
        
        if (!destinataireList.isSelectionEmpty())
        {
            selection = (ArrayList<String>) destinataireList.getSelectedValuesList();
        }
        
        destinataireList.removeAll();
        SortedListModel listModel = (SortedListModel) destinataireList.getModel();
        listModel.clear();
        for (String url : annuaire.getAbrisDistants().keySet()) {
            listModel.add(url);
        }
        destinataireList.setModel(listModel);

        try {
            int[] tabSelectedIndices = new int[selection.size()];
            for (int i = 0; i<selection.size(); i++)
            {
                tabSelectedIndices[i] = listModel.getElementIndex(selection.get(i));
                
            }
            destinataireList.setSelectedIndices(tabSelectedIndices);
        } catch (Exception ex) {
            if (selection == null || listModel.getSize() > 0) {
                destinataireList.setSelectedIndex(0);
            }
        }
        ajusterEtatBoutons();
    }

    protected void ajouterTexteRecu(String texte) {
        try {
            HTMLDocument doc = (HTMLDocument) receptionTextPane.getDocument();
            ((HTMLEditorKit) receptionTextPane.getEditorKitForContentType("text/html")).insertHTML(doc, doc.getLength(), texte, 0, 0, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            afficherErreur("Erreur lors de l'affichage d'un message recu", ex.getMessage());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Abri) {
            for (Message message : ((Abri) o).lireTampon()) {
                ajouterTexteRecu(message.toHTML());
            }
            ((Abri) o).viderTampon();
        } else if (o instanceof Annuaire) {
            remplirListe((Annuaire) o);
        }
        repaint();
    }

    /**
     * Affiche une bo�te de dialogue correspondant � une erreur
     *
     * @param titre Titre de la bo�te de dialogue
     * @param contenu D�tail du message d'erreur
     */
    public void afficherErreur(String titre, String contenu) {
        new ErrorDialog(this, titre, contenu);
    }

    protected class EmissionThread extends Thread {

        JFrame parent;

        EmissionThread(JFrame parent) {
            this.parent = parent;
        }

        @Override
        public void run() {
            while (emettreEnBoucle) {
                try {
                    backend.emettreMessage(emissionTextArea.getText());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new ErrorDialog(parent, "Erreur lors de l'emission du message", ex.getMessage());
                }
            }
        }

    }
}
