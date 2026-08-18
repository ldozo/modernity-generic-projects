# Modernity Generic Projects

Modernity Generic Projects brings a lightweight, folder-centric workflow to Apache NetBeans.

The plugin allows Git repositories and arbitrary folders to be opened as projects, making NetBeans more convenient for working with configuration repositories, infrastructure code, YAML files, scripts, documentation, and other non-traditional project structures.

## Minimum System Requirements

```text
Java 21 or later
Apache NetBeans 30 or later
```

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