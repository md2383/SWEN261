import { Observable, of } from 'rxjs';

export function log(message: string) {
  console.log(`[App] ${message}`);
}

export function handleError<T>(operation = 'operation', result?: T) {
  return (error: any): Observable<T> => {
    console.error(error);
    log(`${operation} failed: ${error.message}`);
    return of(result as T);
  };
}