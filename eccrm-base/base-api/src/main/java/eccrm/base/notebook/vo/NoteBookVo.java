package eccrm.base.notebook.vo;

import eccrm.base.notebook.domain.NoteBook;
/**
* @author shenbb
* @datetime 2014-03-22
*/

public class NoteBookVo extends NoteBook {

	private String title;
	private String content;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
