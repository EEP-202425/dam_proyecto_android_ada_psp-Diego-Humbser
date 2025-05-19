package dam.proyectofinal.mireparto.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
	
    private LocalDateTime timestamp = LocalDateTime.now();    
    private int status;
    private String error;
    private String path;
    private List<ValidationError> errors;

    public ErrorResponse() { }

    public LocalDateTime getTimestamp() { 
    	return timestamp; 
    }
    
    public void setTimestamp(LocalDateTime timestamp) { 
    	this.timestamp = timestamp; 
    }
    
    public int getStatus() { 
    	return status; 
    }
    
    public void setStatus(int status) { 
    	this.status = status; 
    }
    
    public String getError() { 
    	return error; 
    }
    
    public void setError(String error) { 
    	this.error = error;
    }
    
    public String getPath() { 
    	return path; 
    }
    
    public void setPath(String path) { 
    	this.path = path;
    }
    
    public List<ValidationError> getErrors() { 
    	return errors; 
    }
    
    public void setErrors(List<ValidationError> errors) { 
    	this.errors = errors; 
    }

    public static class ValidationError {
    	
        private String field;
        private String message;

        public ValidationError() { }

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() { 
        	return field; 
        }
        
        public void setField(String field) { 
        	this.field = field; 
        }
        
        public String getMessage() { 
        	return message; 
        }
        
        public void setMessage(String message) { 
        	this.message = message; 
        }
        
    }
    
}
