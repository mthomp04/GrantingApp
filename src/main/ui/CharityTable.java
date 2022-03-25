//package ui;
//
//import java.util.ArrayList;
//
//public class CharityTable extends Tables {
//    ArrayList<String> cols = new ArrayList<>();
//
//    public CharityTable() {
//        cols.add("Charity");
//        cols.add("Amount Awarded");
//    }
//
//    @Override
//    public Object getValueAt(int r, int c) {
//        if (c == 0) {
//            return foundation.getCharityList().get(r).getName();
//        } else {
//            return foundation.getCharityList().get(r).totalFunded();
//        }
//    }
//
//    @Override
//    public int getColumnCount() {
//        return 2;
//    }
//
//    @Override
//    public int getRowCount() {
////        return foundation.getCharityList().size();
//    }
//
//    @Override
//    public String getColumnName(int c) {
//        return cols.get(c);
//    }
//
//    public void setValueAt(Object value, int r, int c) {
//        fireTableCellUpdated(r, c);
//    }
//
//    public boolean isCellEditable(int r, int c) {
//        return false;
//    }
//}
//
