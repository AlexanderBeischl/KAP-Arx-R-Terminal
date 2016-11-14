import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;

abstract class Tab {
  Composite tabFolderPage;
  SashForm sash;
  Group layoutGroup;
  Composite layoutComposite;
  String tabName;
  final ArxRTerminal instance;

  SelectionListener selectionListener = new SelectionAdapter() {
    public void widgetSelected(SelectionEvent e) {
      resetEditors();
    }
  };

  TraverseListener traverseListener = new TraverseListener() {
    public void keyTraversed(TraverseEvent e) {
      if (e.detail == SWT.TRAVERSE_RETURN) {
        e.doit = false;
        resetEditors();
      }
    }
  };

  
  Tab(ArxRTerminal instance, String tabName) {
    this.instance = instance;
    this.tabName = tabName;
  }

  
  void createLayout() {
  }

  
  void createLayoutComposite() {
    layoutComposite = new Composite(layoutGroup, SWT.BORDER);
    layoutComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
    createLayout();
  }

 
  void createLayoutGroup() {
  }
  

  Composite createTabFolderPage(TabFolder tabFolder) {
    tabFolderPage = new Composite(tabFolder, SWT.NULL);
    tabFolderPage.setLayout(new FillLayout());
    sash = new SashForm(tabFolderPage, SWT.VERTICAL);
    createLayoutGroup();
   
    return tabFolderPage;
  }


 
  String[] getLayoutDataFieldNames() {
    return new String[] {};
  }

  String getTabText() {
    return "";
  }

 
  void resetEditors() {
    resetEditors(false);
  }

  void resetEditors(boolean tab) {
  }


  void setLayoutData() {
  }

  
  void setLayoutState() {
  }
}