package com.radixshock.radixcore.util.object;

public class Version
{
	private int version;
	private int major;
	private int minor;

	public Version(int version, int major, int minor)
	{
		this.version = version;
		this.major = major;
		this.minor = minor;
	}

	public Version(String versionString)
	{
		int progress = 1;
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < versionString.length(); i++)
		{
			final char c = versionString.charAt(i);

			if (c == '.' || i == versionString.length() - 1)
			{
				if (builder.toString().isEmpty())
				{
					builder.append(c);
				}

				switch (progress)
				{
					case 1:
						version = Integer.parseInt(builder.toString());
						break;
					case 2:
						major = Integer.parseInt(builder.toString());
						break;
					case 3:
						minor = Integer.parseInt(builder.toString());
						break;
				}

				builder = new StringBuilder();
				progress++;
			}

			else
			{
				builder.append(c);
			}
		}
	}

	public boolean isGreaterOrEqual(Version comparator)
	{
		if (version > comparator.version)
		{
			return true;
		}

		else if (major > comparator.major)
		{
			return true;
		}

		else if (major < comparator.major)
		{
			return false;
		}

		else if (minor > comparator.minor)
		{
			return true;
		}

		else if (version == comparator.version && major == comparator.major && minor == comparator.minor)
		{
			return true;
		}

		return false;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Version)
		{
			final Version version = (Version) obj;

			return version.version == this.version && version.major == major && version.minor == minor;
		}

		return false;
	}

	@Override
	public String toString()
	{
		return version + "." + major + "." + minor;
	}
}
