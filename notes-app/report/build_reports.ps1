$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem

function Write-Utf8NoBom {
    param([string]$Path, [string]$Content)
    $utf8NoBom = New-Object System.Text.UTF8Encoding($false)
    [System.IO.File]::WriteAllText($Path, $Content, $utf8NoBom)
}

function New-DocXmlFromLines {
    param([string[]]$Lines)

    $sb = New-Object System.Text.StringBuilder
    [void]$sb.Append('<?xml version="1.0" encoding="UTF-8" standalone="yes"?>')
    [void]$sb.Append('<w:document xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas" xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math" xmlns:v="urn:schemas-microsoft-com:vml" xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing" xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing" xmlns:w10="urn:schemas-microsoft-com:office:word" xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml" xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup" xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk" xmlns:wne="http://schemas.microsoft.com/office/2006/wordml" xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape" mc:Ignorable="w14 w15 wp14">')
    [void]$sb.Append('<w:body>')

    foreach ($line in $Lines) {
        $escaped = [System.Security.SecurityElement]::Escape($line)
        if ([string]::IsNullOrWhiteSpace($line)) {
            [void]$sb.Append('<w:p/>')
        } else {
            [void]$sb.Append('<w:p><w:r><w:t xml:space="preserve">')
            [void]$sb.Append($escaped)
            [void]$sb.Append('</w:t></w:r></w:p>')
        }
    }

    [void]$sb.Append('<w:sectPr><w:pgSz w:w="11906" w:h="16838"/><w:pgMar w:top="1134" w:right="850" w:bottom="1134" w:left="1701" w:header="708" w:footer="708" w:gutter="0"/><w:cols w:space="708"/><w:docGrid w:linePitch="360"/></w:sectPr>')
    [void]$sb.Append('</w:body></w:document>')
    return $sb.ToString()
}

function Build-Docx {
    param(
        [string]$SourceDir,
        [string]$OutDocx,
        [string[]]$Lines
    )

    $tmpRoot = Join-Path ([System.IO.Path]::GetDirectoryName($OutDocx)) ([System.IO.Path]::GetFileNameWithoutExtension($OutDocx) + "_tmp")
    Remove-Item -Recurse -Force -LiteralPath $tmpRoot -ErrorAction SilentlyContinue
    Copy-Item -LiteralPath $SourceDir -Destination $tmpRoot -Recurse -Force

    $docXml = New-DocXmlFromLines -Lines $Lines
    Write-Utf8NoBom -Path (Join-Path $tmpRoot "word\\document.xml") -Content $docXml

    Remove-Item -LiteralPath $OutDocx -Force -ErrorAction SilentlyContinue
    $fs = [System.IO.File]::Open($OutDocx, [System.IO.FileMode]::CreateNew)
    try {
        $zip = New-Object System.IO.Compression.ZipArchive($fs, [System.IO.Compression.ZipArchiveMode]::Create, $false)
        try {
            $files = Get-ChildItem -LiteralPath $tmpRoot -Recurse -File
            foreach ($file in $files) {
                $rel = $file.FullName.Substring($tmpRoot.Length).TrimStart([char]92)
                $entryName = $rel -replace "\\","/"
                $entry = $zip.CreateEntry($entryName, [System.IO.Compression.CompressionLevel]::Optimal)
                $inStream = [System.IO.File]::OpenRead($file.FullName)
                $outStream = $entry.Open()
                try { $inStream.CopyTo($outStream) }
                finally { $outStream.Dispose(); $inStream.Dispose() }
            }
        }
        finally { $zip.Dispose() }
    }
    finally { $fs.Dispose() }

    Remove-Item -Recurse -Force -LiteralPath $tmpRoot -ErrorAction SilentlyContinue
}

$baseDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$lr11Txt = Join-Path $baseDir "lr11_lines.txt"
$lr12Txt = Join-Path $baseDir "lr12_lines.txt"
$lr11Docx = Join-Path $baseDir "ЛР11_NotesApp_Разработка_JUnit.docx"
$lr12Docx = Join-Path $baseDir "ЛР12_NotesApp_Espresso_Profiler.docx"

$lr11Lines = Get-Content -LiteralPath $lr11Txt -Encoding UTF8
$lr12Lines = Get-Content -LiteralPath $lr12Txt -Encoding UTF8

Build-Docx -SourceDir "c:\Users\pupki\.android\task_refs\lr11_sample" -OutDocx $lr11Docx -Lines $lr11Lines
Build-Docx -SourceDir "c:\Users\pupki\.android\task_refs\lr12_sample" -OutDocx $lr12Docx -Lines $lr12Lines

Write-Output "Created:"
Write-Output $lr11Docx
Write-Output $lr12Docx
