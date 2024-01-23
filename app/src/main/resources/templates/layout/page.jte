@import hexlet.code.dto.BasePage
@import hexlet.code.util.NamedRoutes
@import gg.jte.Content
@param Content content
@param BasePage page = null

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Page analyzer</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
          crossorigin="anonymous">
</head>

@if(page != null)
    <div class="alert alert-${page.getFlashType()}" role="alert">
        ${page.getFlash()}
    </div>
@endif

<body>
<div class="container">
    <header class="d-flex justify-content-center py-3">
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a href="${NamedRoutes.rootPath()}" class="nav-link active" aria-current="page">Home</a>
            </li>
            <li class="nav-item">
                <a href="${NamedRoutes.urlsPath()}" class="nav-link">Pages</a>
            </li>
        </ul>
    </header>
</div>

<main>
    ${content}
</main>

<div class="container">
    <footer class="footer py-3 mt-4">
        <ul class="nav justify-content-center border-bottom pb-3 mb-3">
            <li class="nav-item">
                <a href="${NamedRoutes.rootPath()}" class="nav-link px-2 text-body-secondary">Home</a>
            </li>
            <li class="nav-item">
                <a href="${NamedRoutes.urlsPath()}" class="nav-link px-2 text-body-secondary">Pages</a>
            </li>
        </ul>
        <p class="text-center text-body-secondary">
            Created by <a href="https://github.com/MihailGit87">Васильев Михаил</a>
        </p>
    </footer>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
</body>
</html>