#include frex:shaders/api/fragment.glsl
#include frex:shaders/lib/math.glsl

void frx_startFragment(inout frx_FragmentData fragData) {
	float glow = frx_luminance(fragData.spriteColor.rgb) - 0.2;
	glow = clamp(glow * 3, 0, 0.5);
	fragData.emissivity = glow;
}
