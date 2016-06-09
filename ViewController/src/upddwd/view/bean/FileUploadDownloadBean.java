package upddwd.view.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.ServletContext;

import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.util.ResetUtils;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import org.apache.myfaces.trinidad.model.UploadedFile;

public class FileUploadDownloadBean {
    private RichInputText pathBind;

    public FileUploadDownloadBean() {
    }

    /**Method to upload file to actual path on Server*/
    @SuppressWarnings("oracle.jdeveloper.java.nested-assignment")
    private String uploadFile(UploadedFile file) {

        UploadedFile myfile = file;
        String path = null;
        if (myfile == null) {

        } else {
            // All uploaded files will be stored in below path
            FacesContext fctx = FacesContext.getCurrentInstance();
            ServletContext servletCtx = (ServletContext) fctx.getExternalContext().getContext();
            //String dirPath = servletCtx.getRealPath("/");
            path = "D://FileStore//" + myfile.getFilename();
            InputStream inputStream = null;
            try {
                FileOutputStream out = new FileOutputStream(path);
                inputStream = myfile.getInputStream();
                byte[] buffer = new byte[8192];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
                out.close();
            } catch (Exception ex) {
                // handle exception
                ex.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }

        }
        //Returns the path where file is stored
        return path;
    }

    /*****Generic Method to Get BindingContainer**/
    public BindingContainer getBindingsCont() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    /**
     * Generic Method to execute operation
     * */
    public OperationBinding executeOperation(String operation) {
        OperationBinding createParam = getBindingsCont().getOperationBinding(operation);
        return createParam;

    }

    /**Method to Upload File ,called on ValueChangeEvent of inputFile
     * @param vce
     */
    @SuppressWarnings("unchecked")
    public void uploadFileVCE(ValueChangeEvent vce) {
        if (vce.getNewValue() != null) {
            //Get File Object from VC Event
            UploadedFile fileVal = (UploadedFile) vce.getNewValue();
            //Upload File to path- Return actual server path
            String path = uploadFile(fileVal);
            System.out.println(path);
            System.out.println(fileVal.getContentType());
            //Method to insert data in table to keep track of uploaded files
            OperationBinding ob = executeOperation("setFileData");
            ob.getParamsMap().put("name", fileVal.getFilename());
            ob.getParamsMap().put("path", path);
            ob.getParamsMap().put("contTyp", fileVal.getContentType());
            ob.execute();
            // Reset inputFile component after upload
            ResetUtils.reset(vce.getComponent());
        }
    }

    /**Method to download file from actual path
     * @param facesContext
     * @param outputStream
     */
    public void downloadFileListener(FacesContext facesContext, OutputStream outputStream) throws IOException {
        //Read file from particular path, path bind is binding of table field that contains path
        File filed = new File(pathBind.getValue().toString());
        FileInputStream fis;
        byte[] b;
        try {
            fis = new FileInputStream(filed);

            int n;
            while ((n = fis.available()) > 0) {
                b = new byte[n];
                int result = fis.read(b);
                outputStream.write(b, 0, b.length);
                if (result == -1)
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputStream.flush();
    }
 public void del(ActionEvent actionEvent) {
        OperationBinding del = getBindingsCont().getOperationBinding("Delete");
        del.execute();
        OperationBinding commit = getBindingsCont().getOperationBinding("Commit");
        commit.execute(); 
    }
    public void setPathBind(RichInputText pathBind) {
        this.pathBind = pathBind;
    }

    public RichInputText getPathBind() {
        return pathBind;
    }

   
}

