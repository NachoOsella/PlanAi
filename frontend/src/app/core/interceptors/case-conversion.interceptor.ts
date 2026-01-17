import { HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';

// Utility functions for case conversion
const toCamel = (str: string): string => {
  return str.replace(/([-_][a-z])/ig, ($1) => {
    return $1.toUpperCase()
      .replace('-', '')
      .replace('_', '');
  });
};

const isObject = (obj: any): boolean => {
  return obj === Object(obj) && !Array.isArray(obj) && typeof obj !== 'function';
};

const keysToCamel = (obj: any): any => {
  if (isObject(obj)) {
    const n: any = {};
    Object.keys(obj).forEach((k) => {
      n[toCamel(k)] = keysToCamel(obj[k]);
    });
    return n;
  } else if (Array.isArray(obj)) {
    return obj.map((i) => {
      return keysToCamel(i);
    });
  }
  return obj;
};

const toSnake = (str: string): string => {
  return str.replace(/[A-Z]/g, letter => `_${letter.toLowerCase()}`);
};

const keysToSnake = (obj: any): any => {
  if (isObject(obj)) {
    const n: any = {};
    Object.keys(obj).forEach((k) => {
      n[toSnake(k)] = keysToSnake(obj[k]);
    });
    return n;
  } else if (Array.isArray(obj)) {
    return obj.map((i) => {
      return keysToSnake(i);
    });
  }
  return obj;
};

export const caseConversionInterceptor: HttpInterceptorFn = (req, next) => {
  // Convert request body to snake_case
  let newReq = req;
  if (req.body) {
    newReq = req.clone({
      body: keysToSnake(req.body)
    });
  }

  return next(newReq).pipe(
    map((event) => {
      // Convert response body to camelCase
      if (event instanceof HttpResponse && event.body) {
        return event.clone({
          body: keysToCamel(event.body)
        });
      }
      return event;
    })
  );
};
