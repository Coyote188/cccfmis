package cccf.ma.web.zk.override;

import java.util.Iterator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Row;

public class EditableRow extends Row {
	// The edit event
	public static final String ON_EDIT = "onEdit";
	// This constant used to remember the last edit row in grid
	private static final String LAST_EDIT_ROW = "LastEditRow";
	private boolean editable = false;

	public EditableRow() {
		EditableRow.this.addEventListener(ON_EDIT, new EventListener() {
			public void onEvent(Event event) throws Exception {
				// Get last edit row
				EditableRow LastEditRow = (EditableRow) EditableRow.this
						.getGrid().getAttribute(LAST_EDIT_ROW);
				if (LastEditRow != EditableRow.this) {
					EditableRow.this.getGrid().setAttribute(LAST_EDIT_ROW,
							EditableRow.this);
					EditableRow.this.toggleEditable(true);

					if (LastEditRow != null)
						LastEditRow.toggleEditable(true);// Turn last edit row's
															// editable off
				}
			}
		});
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void toggleEditable(boolean applyChange) {
		setEditable(!editable);
		for (Iterator<Component> it = this.getChildren().iterator(); it
				.hasNext();) {
			Component cmp = it.next();
			if (cmp instanceof EditableDiv) {
				Events.sendEvent(new Event(EditableDiv.ON_EDITABLE, cmp,
						applyChange));
			}// .... you can create more control
		}
	}
}
