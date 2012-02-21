package cccf.ma.bpm;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.db.TaskMgmtSession;
import org.jbpm.taskmgmt.exe.TaskInstance;

public class ExtractProcessDiagram {
	static TaskMgmtSession taskMgmtSession;
	static JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
	/**
	　*功能描述：解析指定Node节点的x、y坐标以及该节点的width和height<br>
	　*@param root
	　*@param nodeName
	　*@return int[]
	*/
	private static int[] extractBoxConstraint(Element root,String nodeName)
	{
		
		int[]result=new int[4];
		XPath xPath=new DefaultXPath("//node[@name='"+nodeName+"']");
		Element node=(Element)xPath.selectSingleNode(root);
		result[0]=Integer.valueOf(node.attribute("x").getValue()).intValue()-4;
		result[1]=Integer.valueOf(node.attribute("y").getValue()).intValue()-4;
		result[2]=Integer.valueOf(node.attribute("width").getValue()).intValue()+4;
		result[3]=Integer.valueOf(node.attribute("height").getValue()).intValue()+4;
		return result;
	}
	/**
	 * 功能描述：获取gpd文件中流程图的width和height<br>
	 * @param root
	 * @return
	 */
	private static int[]extractImageDimension(Element root){
		int[]result=new int[2];
		result[0]=Integer.valueOf(root.attribute("width").getValue()).intValue();
		result[1]=Integer.valueOf(root.attribute("height").getValue()).intValue();
		return result;
		}
	
	 /**
	 * 在流程图上高亮显示节点 功能描述：<br>
	 * 
	 * @param taskInstanceId
	 *            任务实例ID
	 * @param gpdPath
	 *            流程图坐标文件路径
	 * @param processImagePath
	 *            流程图路径
	 */
	public static String ProcessImageForCurrentTask(long taskInstanceId,
			String gpdPath, String processImagePath) {
		StringBuffer sbString = new StringBuffer();
		try {
			// 初始化dom4j
			SAXReader reader = new SAXReader();
			File xmlFile = new File(gpdPath);
			Document document = reader.read(xmlFile);

			Element rootDiagramElement =  document.getRootElement();
//			Element rootDiagramElement = new SAXReader().read(gpdPath)
//					.getRootElement();
			// 获取当前TaskInstance
			JbpmContext jbpmContext;
			jbpmContext = jbpmConfiguration.createJbpmContext();
			taskMgmtSession = jbpmContext.getTaskMgmtSession();
			TaskInstance taskInstance = taskMgmtSession.getTaskInstance(taskInstanceId);
//			TaskInstance taskInstance = this.defaultJbpmDAO
//					.findTaskInstance(taskInstanceId);
			// 解析gpd.xml
			int[] boxConstraint = extractBoxConstraint(rootDiagramElement,
					taskInstance.getTask().getTaskNode().getName());
			int[] imageDimension = extractImageDimension(rootDiagramElement);
			// 具体的画图代码
			String imageLink = processImagePath;
			sbString
					.append("<table border=0 cellspacing=0 cellpadding=0 width="
							+ imageDimension[0]
							+ " height="
							+ imageDimension[1]
							+ " style=\"position:relative\">");
			sbString.append("  <tr>");
			sbString.append("    <td width=" + imageDimension[0] + " height="
					+ imageDimension[1] + " style=\"background-image:url(../"
					+ imageLink + ")\" valign=top>");
			sbString.append("    <div style=\"position:absolute;");
			sbString.append("                left:" + boxConstraint[0]
					+ "px; top:" + boxConstraint[1] + "px;width:"
					+ boxConstraint[2] + "px;height:" + boxConstraint[3]
					+ "px;");
			sbString
					.append("                z-index:1;    border-color:RED;    border-width:4; ");
			sbString
					.append("                border-style: inset; background-color: transparent;\">");
			sbString.append("    </div>");
			sbString.append("    </td>");
			sbString.append("  </tr>");
			sbString.append("</table>");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sbString.toString();
	}
}
