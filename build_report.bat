@echo off
rem ------------------------------------------------------------------
rem Build PDF and Word versions of report.tex
rem ------------------------------------------------------------------
setlocal

rem ---- Switch to the folder containing the .tex file ----
cd /d "%~dp0"

rem ---- 1️⃣ Compile PDF (run twice) ----
if not defined HAVE_PDFLATEX (
    echo Checking for pdflatex executable...
    where pdflatex >nul 2>&1
    if errorlevel 1 (
        echo ERROR: pdflatex not found. Install MiKTeX or TeX Live first.
        exit /b 1
    ) else (
        set HAVE_PDFLATEX=1
    )
)

pdflatex -interaction=nonstopmode report.tex >nul
pdflatex -interaction=nonstopmode report.tex >nul

rem ---- 2️⃣ Convert to DOCX (requires pandoc) ----
where pandoc >nul 2>&1
if errorlevel 1 (
    echo ERROR: pandoc not found. Install pandoc first.
    exit /b 1
)
pandoc report.tex -s -o report.docx >nul

rem ---- 3️⃣ (Optional) commit the artefacts ----
rem git add report.pdf report.docx
rem git commit -m "Add compiled PDF and DOCX versions of the report"
rem git push

echo -------------------------------------------------
echo Done! Files generated:
if exist report.pdf echo   - report.pdf
if exist report.docx echo   - report.docx
echo -------------------------------------------------
endlocal
