package taco.klkl.global.error;

public record ErrorResponse(String className, String message) {
	public static ErrorResponse of(String className, String message) {
		return new ErrorResponse(className, message);
	}
}
