/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        "primary": "#6467f2",
        "background-light": "#f6f6f8",
        "background-dark": "#101122",
        // Semantic UI colors
        "sidebar": "#1a1a1a",
        "sidebar-hover": "#252525",
        "surface": "#fcfcfd",
        "surface-dark": "#1e1e2e",
      },
      fontFamily: {
        "display": ["Inter", "sans-serif"]
      },
      borderRadius: {
        "DEFAULT": "0.25rem",
        "lg": "0.5rem",
        "xl": "0.75rem",
        "full": "9999px"
      },
    },
  },
  plugins: [
    require('@tailwindcss/forms'),
    require('@tailwindcss/container-queries'),
  ],
}
