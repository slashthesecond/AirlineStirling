import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// Generated by Together
// Edited by Nganga

/**
 * An interface to SAAMS: Refuelling Supervisor Screen: Inputs events from the
 * Refuelling Supervisor, and displays aircraft information. This class is a
 * controller for the AircraftManagementDatabase: sending it messages to change
 * the aircraft status information. This class also registers as an observer of
 * the AircraftManagementDatabase, and is notified whenever any change occurs in
 * that <<model>> element. See written documentation.
 *
 * @stereotype boundary/view/controller
 * @url element://model:project::SAAMS/design:view:::id15rnfcko4qme4cko4swib
 * @url element://model:project::SAAMS/design:node:::id15rnfcko4qme4cko4swib.node107
 * @url element://model:project::SAAMS/design:view:::id3y5z3cko4qme4cko4sw81
 */
public class RefuellingSupervisor extends JFrame implements Observer, ActionListener {
    /**
     * The Refuelling Supervisor Screen interface has access to the
     * AircraftManagementDatabase.
     *
     * @supplierCardinality 1
     * @clientCardinality 1
     * @label accesses/observes
     * @directed
     */
    private AircraftManagementDatabase aircraftManagementDB;

    private ArrayList<Integer> refuelCodes;

    private JList<String> managementRecordList;
    private JButton refuelledBtn;

    /**
     * Constructor
     */
    public RefuellingSupervisor(AircraftManagementDatabase aircraftManagementDatabase) {
        this.aircraftManagementDB = aircraftManagementDatabase;
        aircraftManagementDatabase.addObserver(this);
        refuelCodes = new ArrayList<Integer>();
        initGUI();
    }

    /**
     * Sets up the GUI for the Refuelling Supervisor View
     */

    private void initGUI() {
        setTitle("Refuelling Supervisor");
        setSize(250, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(1150, 610);

        JPanel jpGC = new JPanel();

        managementRecordList = new JList<String>(new DefaultListModel<String>());
        JScrollPane scrollList = new JScrollPane(managementRecordList);
        managementRecordList.setFixedCellWidth(150); // Stops resizing when changing tabs
        managementRecordList.setVisibleRowCount(10);

        jpGC.add(scrollList);
        updateManagementRecordList();

        refuelledBtn = new JButton("Refuelled");
        refuelledBtn.addActionListener(this);
        jpGC.add(refuelledBtn);

        getContentPane().add(jpGC);
        setVisible(true);

    }

    /**
     * Updates information held and displayed regarding Management Records
     */
    private void updateManagementRecordList() {
        refuelCodes.clear();
        refuelCodes.addAll(Arrays.asList(aircraftManagementDB.getWithStatus(ManagementRecord.READY_REFUEL)));

        String[] flightCodes = new String[refuelCodes.size()];
        for (int i = 0; i < flightCodes.length; i++) {
            flightCodes[i] = aircraftManagementDB.getFlightCode(refuelCodes.get(i)) + ": " + aircraftManagementDB.getStatusString(refuelCodes.get(i));
        }
        managementRecordList.setListData(flightCodes);
        // Update size of display
        managementRecordList.updateUI();

    }

    @Override
    public void update(Observable arg0, Object arg1) {
        updateManagementRecordList();
    }

    @Override
    public void actionPerformed(ActionEvent evn) {
        int index = managementRecordList.getSelectedIndex();
        if (evn.getSource() == refuelledBtn) {
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Please select an aircraft");
            } else {
                int mCode = refuelCodes.get(index);
                int status = aircraftManagementDB.getStatus(refuelCodes.get(index));
                if (status == ManagementRecord.READY_REFUEL) {
                    aircraftManagementDB.setStatus(mCode, ManagementRecord.READY_PASSENGERS);
                } else {
                    JOptionPane.showMessageDialog(this, "Aircraft not ready for refuel");
                }
            }
        }
    }

}
