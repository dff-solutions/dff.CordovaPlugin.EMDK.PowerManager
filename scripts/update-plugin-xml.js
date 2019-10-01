const fs = require("fs");
const path = require("path");
const xml2js = require("xml2js");

const projectDir = path.join(__dirname, "..");
const pluginXmlPath = path.join(projectDir, "plugin.xml");
const androidSrcDir = path.join(projectDir, "src", "main", "java");
const androidDestDir = "src";

console.log(androidSrcDir);

const androidSrcFiles = readDir(androidSrcDir)
    .sort()
    .map((file) => {
        const src = unixfyPath(
            file.substring(projectDir.length + 1)
        );
        const destFile = unixfyPath(
            path.join(androidDestDir, file.substring(androidSrcDir.length + 1))
        );
        const targetDir = unixfyPath(
            path.dirname(destFile)
        );

        return {
            $: {
                src,
                "target-dir": targetDir
            }
        };
    });

const pluginXmlData = fs.readFileSync(pluginXmlPath);

xml2js.parseString(pluginXmlData, (err, json) => {
    if (err) {
        console.error(err);
    }

    const builder = new xml2js.Builder();
    const xmlPlatformAndroid = json.plugin.platform.filter((p) => p.$.name == "android").pop();
    const oldSourceFiles = xmlPlatformAndroid["source-file"];

    // keep system libs
    const sourceLibs = oldSourceFiles
        .filter((oldFile) => oldFile.$.src.endsWith(".jar"));
    xmlPlatformAndroid["source-file"] = androidSrcFiles.concat(sourceLibs);

    const xml = builder.buildObject(json);

    fs.writeFileSync(pluginXmlPath, xml);
});

function readDir(dir) {
    // console.debug(`read dir ${dir}`);
    let result = [];

    if (fs.existsSync(dir)) {
        fs
            .readdirSync(dir)
            .forEach((file) => {
                const filePath = path.join(dir, file)
                const stat = fs.lstatSync(filePath);

                if (stat.isDirectory()) {
                    result = result.concat(readDir(filePath));
                }
                else if (stat.isFile()) {
                    result.push(filePath);
                }
            });
    }
    else {
        console.warn(`directory not found ${dir}`);
    }

    return result;
}

function unixfyPath(path) {
    return path.replace(/\\/g, '/')
}
