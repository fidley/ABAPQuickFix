package com.abapblog.adt.quickfix;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import javax.swing.text.Position;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.ITextInputListener;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.jface.text.quickassist.IQuickAssistProcessor;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.MarkerAnnotation;
import org.eclipse.ui.texteditor.ResourceMarkerAnnotationModel;

import com.sap.adt.tools.abapsource.ui.sources.editors.AdtProjectionViewer;
import com.sap.adt.communication.exceptions.OutOfSessionsException;
import com.sap.adt.project.IAdtCoreProject;
import com.sap.adt.project.ui.util.ProjectUtil;
import com.sap.adt.tools.abapsource.sources.AdtSourceServicesFactory;
import com.sap.adt.tools.abapsource.sources.IAdtSourceServicesFactory;
import com.sap.adt.tools.abapsource.sources.codeelementinformation.ICodeElementInformationBackendService;
import com.sap.adt.tools.abapsource.sources.content.AdtSourceDataObjectLoaderForSimpleObjects;
import com.sap.adt.tools.abapsource.sources.objectstructure.IObjectStructureService;
import com.sap.adt.tools.abapsource.ui.internal.sources.outline.AdtStructuralInfoService;
import com.sap.adt.tools.abapsource.ui.sources.outline.IAdtStructuralInfoService;
public class abapAssistProcessor implements IQuickAssistProcessor {

	@Override
	public boolean canAssist(IQuickAssistInvocationContext arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canFix(Annotation arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public ICompletionProposal[] computeQuickAssistProposals(IQuickAssistInvocationContext context) {
		int lenght = context.getLength();
		int offset = context.getOffset();
//		AdtProjectionViewer viewer = (AdtProjectionViewer)context.getSourceViewer();
//		int mark = viewer.getMark();
//		ITextInputListener TIL = new ITextInputListener() {
//
//			@Override
//			public void inputDocumentChanged(IDocument arg0, IDocument arg1) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void inputDocumentAboutToBeChanged(IDocument arg0, IDocument arg1) {
//				// TODO Auto-generated method stub
//
//			}
//		};
//		viewer.addTextInputListener(TIL);
//		IAnnotationModel annotationModel = viewer.getAnnotationModel();
//		ResourceMarkerAnnotationModel aM = (ResourceMarkerAnnotationModel)annotationModel;
//		Iterator<Annotation> aI  = aM.getAnnotationIterator();
//		if (aI.hasNext())
//		{
//		 Annotation annotation = aI.next();
//		 String  anType = annotation.getType();
//		 String  anText = annotation.getText();
//		 String  anClass  = annotation.getClass().toString();
//		 MarkerAnnotation markerAn = (MarkerAnnotation) annotation;
//		 IMarker marker2 = markerAn.getMarker();
//		 org.eclipse.jface.text.Position anPosition = aM.getPosition(annotation);
//		 int anOffset = anPosition.getOffset();
//		 int anLenght = anPosition.getLength();
//		 aI.toString();
//		}
//		//annotationModel.
//		int x = context.getSourceViewer().getSelectedRange().x;
//		int y = context.getSourceViewer().getSelectedRange().y;
//		ITextOperationTarget target = context.getSourceViewer().getTextOperationTarget();
//		IFindReplaceTarget FRTarget = context.getSourceViewer().getFindReplaceTarget();
//		String SelectionText = FRTarget.getSelectionText();
//		int topIndex = context.getSourceViewer().getTopIndex();
//		int topIndexSO = context.getSourceViewer().getTopIndexStartOffset();
//		int bottomIndex = context.getSourceViewer().getBottomIndex();
//		int bottomIndexEO = context.getSourceViewer().getBottomIndexEndOffset();
//		String targetString = target.toString();
		String sourceCode = context.getSourceViewer().getDocument().get();
		//String[] test999 = context.getSourceViewer().getDocument().getPositionCategories();
		//String[] delimiters = context.getSourceViewer().getDocument().getLegalLineDelimiters();
		//String[] legalContentTYpes = context.getSourceViewer().getDocument().getLegalContentTypes();

		//context.getSourceViewer().getDocument().set(codeParser.parse(sourceCode));
//		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//		if (window != null) {
//
//			IWorkbenchPage page = window.getActivePage();
//			IEditorPart editor = page.getActiveEditor();
//			IEditorInput input = editor.getEditorInput();
//			ISelection Selection = editor.getEditorSite().getSelectionProvider().getSelection();
//			String classString = Selection.getClass().toString();
//
//
//		}
//
		List<ICompletionProposal> proposals = new ArrayList<>();
		Image image = null;
		CompletionProposal cProp = new CompletionProposal(codeParser.parse(sourceCode), 0, sourceCode.length(), 5, image, "displayString", null, "additionalProposalInfo");
		proposals.add( cProp );
		proposals.add( new CompletionProposal("abapBlogTest",1,4,4) );
		return proposals.toArray(new ICompletionProposal[0]);
	}

	public static String getProjectName() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IWorkbenchWindow window = page.getWorkbenchWindow();
		ISelection ADTselection = window.getSelectionService().getSelection();
		IProject project = ProjectUtil.getActiveAdtCoreProject(ADTselection, null, null,
				IAdtCoreProject.ABAP_PROJECT_NATURE);
		if (project != null) {
			return project.getName();
		} else {
			return "";
		}
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}


}
