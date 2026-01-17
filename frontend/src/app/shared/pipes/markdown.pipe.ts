import { Pipe, PipeTransform } from '@angular/core';
import { marked } from 'marked';
import DOMPurify from 'dompurify';

@Pipe({
  name: 'markdown',
  standalone: true,
})
export class MarkdownPipe implements PipeTransform {
  constructor() {
    // Configure marked for safe rendering
    marked.setOptions({
      breaks: true,
      gfm: true,
    });
  }

  transform(value: string | null | undefined): string {
    if (!value) return '';

    try {
      const rawHtml = marked.parse(value) as string;
      // Sanitize HTML to prevent XSS
      return DOMPurify.sanitize(rawHtml, {
        USE_PROFILES: { html: true },
        ALLOWED_TAGS: [
          'p', 'br', 'strong', 'em', 'u', 's', 'code', 'pre',
          'h1', 'h2', 'h3', 'h4', 'h5', 'h6',
          'ul', 'ol', 'li', 'blockquote',
          'a', 'span', 'div',
        ],
        ALLOWED_ATTR: ['href', 'target', 'rel', 'class'],
      });
    } catch {
      return value;
    }
  }
}
