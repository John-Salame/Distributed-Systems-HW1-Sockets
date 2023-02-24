/**
 * Class GRPCErrorCodes
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * API version 1
 * Description: Convert an Exception to an error code or an error code to an Exception
 * Status documentation https://grpc.github.io/grpc-java/javadoc/io/grpc/Status.html
 */

package com.jsala.common.transport.grpc;
import java.io.IOException;
import java.util.NoSuchElementException;
import io.grpc.Status;
import io.grpc.Status.Code;

public class GRPCErrorCodes {

	public static Status generateStatus(Exception e) {
		// error code 0 = ok
		// error code 3 = invalid argument -> IllegalArgumentException
		// error code 5 = not found -> NoSuchElementException
		// error code 7 = permission denied -> UnsupportedOperationException
		// error code 2 = unknown -> IOException
		Status status;
		if (e instanceof IllegalArgumentException)
			status = Status.INVALID_ARGUMENT;
		else if (e instanceof NoSuchElementException)
			status = Status.NOT_FOUND;
		else if (e instanceof UnsupportedOperationException)
			status = Status.PERMISSION_DENIED;
		else if (e instanceof IOException)
			status = Status.UNKNOWN;
		else
			status = Status.UNKNOWN;
		return status.withDescription(e.getMessage());
	}

	public static void generateExceptionFromStatus(Status status) throws IOException {
		// error code 0 = ok
		// error code 3 = invalid argument -> IllegalArgumentException
		// error code 5 = not found -> NoSuchElementException
		// error code 7 = permission denied -> UnsupportedOperationException
		// error code 2 = unknown -> IOException
		Status.Code code = status.getCode();
		String message = status.getDescription();
		switch(code) {
			case OK:
				return; // ok
			case INVALID_ARGUMENT:
				throw new IllegalArgumentException(message);
			case PERMISSION_DENIED:
				throw new UnsupportedOperationException(message);
			case UNKNOWN:
				throw new IOException(message);
		}
	}
}
