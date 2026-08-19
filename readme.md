# Modernity Generic Projects

![Enginar Logo](src/main/resources/enginaar/modernity/genericprojects/enginar_logo.svg)

Modernity Generic Projects brings a lightweight, folder-centric workflow to Apache NetBeans.

The plugin allows Git repositories and arbitrary folders to be opened as projects, making NetBeans more convenient for working with configuration repositories, infrastructure code, YAML files, scripts, documentation, and other non-traditional project structures.

## Minimum System Requirements

```text
Java 21 or later
Apache NetBeans 30 or later
```

---

## How It Works

The plugin registers a `ProjectFactory` (`GenericProjectFactory`) that recognises a directory as a project when any of the following is true:

- It is a Git repository (contains a `.git` entry).
- It contains a permanent `nbproject/project.xml` whose type is `enginaar.modernity.genericprojects`.
- It contains the temporary marker file `.netbeans-folder-project`.

Once recognised, the folder is wrapped in a lightweight `GenericProject` whose services (information, logical view, operations, actions) are provided through the project lookup.

### Project Icons

- **Git repositories** show the Git repository icon:  
  ![Git Icon](src/main/resources/enginaar/modernity/genericprojects/git-icon_16.svg)

- **All other generic projects** (temporary folder projects and permanent converted projects) show the enginar logo.

- The icon is resolved by `GenericProjectInformation` and the project tree root node; when the icon resources are unavailable a default project icon is used as a fallback.

### Project Tree Filtering

The logical view delegates to the standard folder node of the wrapped directory, so the tree reflects the folder contents using NetBeans' default data-object nodes. Internal bookkeeping entries are hidden from the tree:

```text
nbproject
.netbeans-folder-project
.git
```

The filtering is applied recursively at every level via `GenericProjectLogicalViewProvider.FilteredChildren`.

---

## Features

### Open Folder

Open any folder directly from NetBeans.

```text
File → Open Folder...
```

The selected folder can be:

- A Git repository
- An existing NetBeans project
- Any ordinary folder

---

### Git Repository Detection

Folders containing a `.git` directory are automatically recognized as projects.

```text
my-repo
├─ .git
├─ README.md
└─ values.yaml
```

Git repositories can be opened through:

```text
File → Open Project...
```

or

```text
File → Open Folder...
```

---

### Open As Folder

When a selected folder is not recognized as a project, you can choose:

```text
Open As Folder
```

A temporary project marker is created:

```text
.netbeans-folder-project
```

The folder is then opened as a project, enabling:

- Project view
- File navigation
- Global search
- Editor integration
- Existing NetBeans features

The marker is automatically removed when the project is closed.

---

### Convert To Project

Folders can be converted into permanent NetBeans projects.

```text
Convert To Project
```

creates:

```text
nbproject/
└─ project.xml
```

After conversion, the project can be opened normally using:

```text
File → Open Project...
```

---

## Examples

### Git Repository

```text
helm-charts
├─ .git
├─ Chart.yaml
└─ templates
```

Automatically opened as a project.

### Temporary Folder Project

```text
terraform-infra
├─ main.tf
├─ variables.tf
└─ outputs.tf
```

Opened using:

```text
Open Folder → Open As Folder
```

### Permanent Folder Project

```text
docs
├─ architecture
├─ diagrams
└─ nbproject
    └─ project.xml
```

Opened as a regular project.

---

## Development

### Building

```text
mvn clean package
```

### Running the Tests

The project includes a plain JUnit test suite that runs without the full NetBeans module system. The test setup injects a mock `ProjectManagerImplementation` into the global lookup through the `org.openide.util.Lookup` system property configured in `pom.xml`.

```text
mvn test
```

The tests cover:

- Folder marker creation, removal and idempotency (`FolderProjectMarkerTest`)
- Project conversion to a permanent project (`ProjectConverterTest`)
- Project recognition and loading (`GenericProjectFactoryTest`)
- Project metadata and data files (`GenericProjectOperationsTest`)
- Action provider commands (`GenericProjectActionProviderTest`)
- Root node naming (`GenericProjectRootNodeTest`)
- Project information, icons and resources (`GenericProjectTest`)
- Logical view tree filtering and path resolution (`GenericProjectLogicalViewProviderTest`)

---

## Why?

Many repositories today contain:

- Kubernetes manifests
- Helm charts
- Terraform configurations
- Docker files
- CI/CD pipelines
- Documentation

These folders often do not contain Maven, Gradle, or traditional NetBeans project metadata.

Modernity Generic Projects makes these folders first-class citizens inside NetBeans.

---

## Compatibility

Tested with:

```text
Apache NetBeans 30
Apache NetBeans 31
Java 21
```

---

## Future Ideas

- Follow my GitHub projects

---

## Author

Kenan Erarslan

```text
kenan@enginaar.com
```

---

## License

MIT License