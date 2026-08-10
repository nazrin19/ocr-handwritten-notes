# Handwritten Notes OCR

A pipeline that extracts text from photos of handwritten notes using Microsoft's TrOCR model, with custom line-segmentation to handle full-page images (TrOCR only reads single lines natively).

## How it works

1. **Adaptive thresholding** — converts the image to black/white, using a local (not global) threshold to handle uneven lighting and glare in real phone photos.
2. **Line segmentation** — uses a horizontal projection profile (row-by-row ink density) to automatically detect and crop individual lines of text.
3. **OCR per line** — each cropped line is passed through `microsoft/trocr-large-handwritten` to extract text.
4. **Cleanup** — filters out junk/empty detections (like ruling lines) before joining the final text.

## Tools used
- Python, Google Colab
- HuggingFace Transformers (TrOCR)
- OpenCV (image processing, adaptive thresholding)
- NumPy (projection profile analysis)

## Known limitations
- Struggles with dense, small handwriting where lines are packed close together
- Ink bleed-through from the back of a page can produce garbled text
- Table/grid layouts with skewed rows aren't segmented cleanly (built for paragraph-style notes, not structured tables)

## Example
[Add a before/after screenshot or sample output here]