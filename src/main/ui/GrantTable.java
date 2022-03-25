//package ui;
//
//class GrantTable extends Tables {
//
//    public GrantTable() {
//        cols.add("Name");
//        cols.add("Status");
//        cols.add("Amount Awarded");
//    }
//
//    @Override
//    public Object getValueAt(int r, int c) {
//        switch (c) {
//            case (0): {
//                if (!foundation.getCharityList().isEmpty()) {
//                    return GrantTrackingApplicationUI.charity.getGrants().get(r).getGrantName();
//                } else {
//                    return 0;
//                }
//            }
//            case (1): {
//                if (!foundation.getCharityList().isEmpty()) {
//                    return GrantTrackingApplicationUI.charity.getGrants().get(r).getStatus();
//                } else {
//                    return 0;
//                }
//            }
//            case (2): {
//                if (!foundation.getCharityList().isEmpty()) {
//                    return GrantTrackingApplicationUI.charity.getGrants().get(r).getAmountGranted();
//                } else {
//                    return 0;
//                }
//            }
//        }
//        return 0;
//    }
//
//    @Override
//    public int getColumnCount() {
//        return 3;
//    }
//
//    @Override
//    public int getRowCount() {
//        if (!foundation.getCharityList().isEmpty()) {
//            return GrantTrackingApplicationUI.charity.getGrants().size();
//        } else {
//            return 0;
//        }
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
//
//}
