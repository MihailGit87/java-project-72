@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlPage
@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <div class="container-lg mt-5">
        <h2>Page ${page.getName()}</h2>
        <table class="table mt-3">
            <tbody>
            <tr>
                <th scope="row">ID</th>
                <td>${page.getId()}</td>
            </tr>
            <tr>
                <th scope="row">Name</th>
                <td>${page.getName()}</td>
            </tr>
            <tr>
                <th scope="row">Created at</th>
                <td>${page.getCreatedAt().toString()}</td>
            </tr>
            </tbody>
        </table>

        <h2 class="mt-5">Checks</h2>

        <form method="post" action="${NamedRoutes.urlChecksPath(page.getId())}">
            <button type="submit" class="btn btn-primary">Run check!</button>
        </form>

        <table class="table mt-3">
            <thead>
            <tr>
                <th class="col">ID</th>
                <th class="col">Status code</th>
                <th class="col">Title</th>
                <th class="col">H1</th>
                <th class="col">Description</th>
                <th class="col">Check date</th>
            </tr>
            </thead>
            <tbody>
            @for(var urlCheck: page.getUrlChecks())
                <tr>
                    <td>${urlCheck.getId()}</td>
                    <td>${urlCheck.getStatusCode()}</td>
                    <td>${urlCheck.getTitle()}</td>
                    <td>${urlCheck.getH1()}</td>
                    <td>${urlCheck.getDescription()}</td>
                    <td>${urlCheck.getCreatedAt().toString()}</td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`
)