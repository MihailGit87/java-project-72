@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlsPage
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <div class="container-lg mt-5">
        <h2>Pages</h2>
        <table class="table table-striped mt-3">
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">Name</th>
                <th scope="col">Last check</th>
                <th scope="col">Response code</th>
            </tr>
            </thead>
            <tbody>
            @for(var url: page.getUrls())
                <tr>
                    <th scope="row">${url.getId()}</th>
                    <td>
                        <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
                    </td>
                    <td>${page.getChecks().isEmpty() || page.getChecks().get(url.getId()) == null ? "" : page.getChecks().get(url.getId()).getCreatedAt().toString()}</td>
                    <td>${page.getChecks().isEmpty() || page.getChecks().get(url.getId()) == null ? "" : page.getChecks().get(url.getId()).getStatusCode().toString()}</td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`
)