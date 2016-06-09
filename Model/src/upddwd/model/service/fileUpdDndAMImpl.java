package upddwd.model.service;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.ViewObjectImpl;

import upddwd.model.service.common.fileUpdDndAM;


// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Sat Aug 02 13:24:23 IST 2014
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class fileUpdDndAMImpl extends ApplicationModuleImpl implements fileUpdDndAM {
    /**
     * This is the default constructor (do not remove).
     */
    public fileUpdDndAMImpl() {
    }

    /**
     * Container's getter for FileUpdDwn1.
     * @return FileUpdDwn1
     */
    public ViewObjectImpl getFileUpdDwn1() {
        return (ViewObjectImpl) findViewObject("FileUpdDwn1");
    }

    /**Method to set file path and name
     * @param name
     * @param path
     */
    public void setFileData(String name, String path,String contTyp) {
        ViewObject fileVo = this.getFileUpdDwn1();
        Row newRow = fileVo.createRow();
        newRow.setAttribute("FileName", name);
        newRow.setAttribute("Path", path);
        newRow.setAttribute("ContentType", contTyp);
        fileVo.insertRow(newRow);
        this.getDBTransaction().commit();
        fileVo.executeQuery();
    }
}
