#include frex:shaders/api/fragment.glsl
#include frex:shaders/lib/math.glsl

void frx_startFragment(inout frx_FragmentData fragData) {
	float a = abs(fragData.spriteColor.g - fragData.spriteColor.r);
	float b = abs(fragData.spriteColor.g - fragData.spriteColor.b);
	fragData.emissivity = (fragData.spriteColor.g > 0.3) ? 0.6 : 0;
}
